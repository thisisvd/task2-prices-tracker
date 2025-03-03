package com.example.task2_prices_tracker.data;

import static com.example.task2_prices_tracker.utils.Constants.RESULT_SUCCESS;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.task2_prices_tracker.core.NetworkUtils;
import com.example.task2_prices_tracker.core.Resource;
import com.example.task2_prices_tracker.data.local.EncryptedSharedPreference;
import com.example.task2_prices_tracker.data.local.room.PricesDao;
import com.example.task2_prices_tracker.data.network.ApiImpl;
import com.example.task2_prices_tracker.domain.Prices;
import com.example.task2_prices_tracker.domain.User;
import com.example.task2_prices_tracker.domain.UserResponse;
import com.example.task2_prices_tracker.utils.CoroutineHelper;

import java.util.List;

import javax.inject.Inject;

import jakarta.inject.Singleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

@Singleton
public class MainRepository {

    // vars
    private final ApiImpl apiImpl;
    private final EncryptedSharedPreference encryptedSharedPreference;
    private final PricesDao pricesDao;
    private final NetworkUtils networkUtils;

    // inject classes
    @Inject
    public MainRepository(ApiImpl apiImpl, EncryptedSharedPreference encryptedSharedPreference, PricesDao pricesDao, NetworkUtils networkUtils) {
        this.apiImpl = apiImpl;
        this.encryptedSharedPreference = encryptedSharedPreference;
        this.pricesDao = pricesDao;
        this.networkUtils = networkUtils;
    }

    // login user
    public LiveData<Resource<String>> loginUser(User user) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        liveData.postValue(new Resource.Loading<>());

        apiImpl.loginUser(user).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    Timber.d("Response : %s", token);
                    encryptedSharedPreference.saveToken(token);
                    liveData.postValue(new Resource.Success<>(RESULT_SUCCESS));
                } else {
                    String errorMsg = "Login Failed : " + response.code() + ", " + response.message();
                    Timber.d(errorMsg);
                    if (response.code() == 401) {
                        errorMsg = "Invalid username or password";
                    }
                    liveData.postValue(new Resource.Error<>(errorMsg));
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable throwable) {
                Timber.d("Login Error : %s", throwable.getMessage());
                liveData.postValue(new Resource.Error<>("Network error occurred!"));
            }
        });

        return liveData;
    }

    // get Prices lists
    public LiveData<Resource<List<Prices>>> getPrices() {
        MutableLiveData<Resource<List<Prices>>> liveData = new MutableLiveData<>();
        liveData.postValue(new Resource.Loading<>());

        if (networkUtils.isNetworkAvailable()) {
            String token = encryptedSharedPreference.getToken();

            if (token != null) {
                apiImpl.getPrices(token).enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Prices>> call, @NonNull Response<List<Prices>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (!response.body().isEmpty()) {
                                storeDataInLocalDB(response.body());
                                liveData.postValue(new Resource.Success<>(response.body()));
                            } else {
                                liveData.postValue(new Resource.Error<>("Empty List"));
                            }
                        } else {
                            Timber.d("Prices Failed : " + response.code() + ", " + response.message());
                            liveData.postValue(new Resource.Error<>("Prices Failed : " + response.code() + ", " + response.message()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Prices>> call, @NonNull Throwable throwable) {
                        Timber.d("Prices Error : %s", throwable.getMessage());
                        liveData.postValue(new Resource.Error<>("Network error occurred!"));
                    }
                });
            } else {
                Timber.d("Prices Error : Empty Token.");
                liveData.postValue(new Resource.Error<>("Prices Failed : Empty Token."));
            }
        } else {
            CoroutineHelper.INSTANCE.runInBackground(v -> {
                List<Prices> prices = pricesDao.getAllPrices();
                if (!prices.isEmpty()) {
                    liveData.postValue(new Resource.Success<>(prices));
                } else {
                    Timber.d("Prices Error : Local data empty.");
                    liveData.postValue(new Resource.Error<>("Prices Failed : Local data empty."));
                }
                return null;
            });
        }

        return liveData;
    }

    // search in prices
    public LiveData<Resource<List<Prices>>> searchInPrices(String searchQuery) {
        MutableLiveData<Resource<List<Prices>>> liveData = new MutableLiveData<>();
        liveData.postValue(new Resource.Loading<>());

        CoroutineHelper.INSTANCE.runInBackground(v -> {
            List<Prices> prices = pricesDao.searchInPrices(searchQuery);
            liveData.postValue(new Resource.Success<>(prices));
            return null;
        });

        return liveData;
    }

    // store prices in local db
    private void storeDataInLocalDB(List<Prices> prices) {
        CoroutineHelper.INSTANCE.runInBackground(v -> {
            pricesDao.deleteAllPrices();
            for (Prices price : prices) {
                pricesDao.insertPrices(price);
            }
            return null;
        });
    }

    public boolean hasToken() {
        return encryptedSharedPreference.getToken() != null;
    }

    // logout user
    public LiveData<Resource<String>> logout() {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        liveData.postValue(new Resource.Loading<>());

        try {
            encryptedSharedPreference.removeToken();
            liveData.postValue(new Resource.Success<>(RESULT_SUCCESS));
        } catch (Exception e) {
            Timber.d("Error : %s", e.getMessage());
            liveData.postValue(new Resource.Error<>(e.getMessage()));
        }

        return liveData;
    }
}