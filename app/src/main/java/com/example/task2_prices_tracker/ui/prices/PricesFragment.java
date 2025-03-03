package com.example.task2_prices_tracker.ui.prices;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.task2_prices_tracker.R;
import com.example.task2_prices_tracker.core.Resource;
import com.example.task2_prices_tracker.databinding.FragmentPricesBinding;
import com.example.task2_prices_tracker.ui.adapter.PricesAdapter;
import com.example.task2_prices_tracker.ui.viewmodel.MainViewModel;
import com.google.android.material.snackbar.Snackbar;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class PricesFragment extends Fragment {

    // view binding
    private FragmentPricesBinding binding;

    // view model
    private MainViewModel mainViewModel;

    // transaction adapter
    private PricesAdapter pricesAdapter;

    // on back pressed counter
    private int onBackPressCount = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPricesBinding.inflate(inflater, container, false);
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init view model
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // init menu provider
        addMenuProvider();

        // call prices api
        mainViewModel.getPrices();

        // initialize view models
        initViewModels();

        // initialize recycler view
        initRecyclerView();

        // handling on back pressed
        handleOnBackPressed();
    }

    // init view models
    private void initViewModels() {
        mainViewModel.logout.observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                if (resource instanceof Resource.Loading) {
                    binding.progressCircular.setVisibility(View.VISIBLE);
                    Timber.d("Logout Loading!");
                } else if (resource instanceof Resource.Success) {
                    binding.progressCircular.setVisibility(View.GONE);
                    Navigation.findNavController(binding.getRoot()).popBackStack();
                } else if (resource instanceof Resource.Error) {
                    binding.progressCircular.setVisibility(View.GONE);
                    Timber.d("Login Error : %s", resource.getMessage());
                    Snackbar.make(binding.getRoot(), "Error occurred while logout!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        mainViewModel.prices.observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                if (resource instanceof Resource.Loading) {
                    binding.progressCircular.setVisibility(View.VISIBLE);
                    Timber.d("Prices Loading!");
                } else if (resource instanceof Resource.Success) {
                    binding.progressCircular.setVisibility(View.GONE);
                    if (!resource.getData().isEmpty()) {
                        pricesAdapter.submitList(resource.getData());
                    }
                } else if (resource instanceof Resource.Error) {
                    binding.progressCircular.setVisibility(View.GONE);
                    Timber.d("Prices Error : %s", resource.getMessage());
                    Snackbar.make(binding.getRoot(), resource.getMessage(), Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    // init recycler view
    private void initRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        pricesAdapter = new PricesAdapter();
        binding.recyclerView.setAdapter(pricesAdapter);
    }

    // handle on back pressed
    private void handleOnBackPressed() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onBackPressCount++;
                if (onBackPressCount == 1) {
                    Snackbar.make(binding.getRoot(), "Press back again to close the app.", Snackbar.LENGTH_SHORT).show();
                } else {
                    requireActivity().finish();
                }
            }
        });
    }

    private void addMenuProvider() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu, menu);

                // Get the search item and its SearchView
                MenuItem searchItem = menu.findItem(R.id.action_search);
                SearchView searchView = (SearchView) searchItem.getActionView();

                // Handle search query input
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        mainViewModel.searchInPrices(newText);
                        return true;
                    }
                });
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_logout) {
                    mainViewModel.logout();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}