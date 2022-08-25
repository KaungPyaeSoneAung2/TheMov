package com.exam.themov.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.exam.themov.repository.PopularRepository

class ViewModelFactory(private val popularRepository: PopularRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(popularRepository) as T
    }
}