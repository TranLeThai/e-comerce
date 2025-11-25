package com.example.e_comerce.core.data.local.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.e_comerce.core.viewmodel.CartViewModel;

public class CartViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;

    public CartViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CartViewModel(application);
    }
}
