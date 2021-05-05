package com.example.firstapp.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.firstapp.data.api.ApiHelper
import com.example.firstapp.data.repository.MainRepository
import com.example.firstapp.ui.viewmodel.MainViewModel

class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(MainRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}