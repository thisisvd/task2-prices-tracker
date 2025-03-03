package com.example.task2_prices_tracker.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.task2_prices_tracker.core.Resource;
import com.example.task2_prices_tracker.data.MainRepository;
import com.example.task2_prices_tracker.domain.Prices;
import com.example.task2_prices_tracker.domain.User;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {

    private final MainRepository mainRepository;

    @Inject
    public MainViewModel(MainRepository mainRepository) {
        this.mainRepository = mainRepository;
    }

    // login live data
    private final MutableLiveData<Resource<String>> _login = new MutableLiveData<>();
    public LiveData<Resource<String>> login = _login;

    // login user
    public void loginUser(User user) {
        mainRepository.loginUser(user).observeForever(_login::setValue);
    }

    // prices live data
    private final MutableLiveData<Resource<List<Prices>>> _prices = new MutableLiveData<>();
    public LiveData<Resource<List<Prices>>> prices = _prices;

    // prices lists
    public void getPrices() {
        mainRepository.getPrices().observeForever(_prices::setValue);
    }

    // search in prices
    public void searchInPrices(String searchQuery) {
        mainRepository.searchInPrices(searchQuery).observeForever(_prices::setValue);
    }

    // clear login state after successful user login
    public void clearLoginState() {
        _login.setValue(null);
    }

    // check token
    public boolean hasToken() {
        return mainRepository.hasToken();
    }

    // logout live data
    private final MutableLiveData<Resource<String>> _logout = new MutableLiveData<>();
    public LiveData<Resource<String>> logout = _logout;

    // logout
    public void logout() {
        mainRepository.logout().observeForever(_logout::setValue);
    }
}