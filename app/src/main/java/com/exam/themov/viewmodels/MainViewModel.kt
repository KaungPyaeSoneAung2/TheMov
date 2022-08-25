package com.exam.themov.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exam.themov.models.Anime.AnimeData
import com.exam.themov.models.PopularData
import com.exam.themov.repository.PopularRepository
import kotlinx.coroutines.launch

class MainViewModel(private val popularRepository: PopularRepository): ViewModel() {
    init {
        viewModelScope.launch {
            popularRepository.getAnime()
        }
    }
    val anime : LiveData<AnimeData>
    get() = popularRepository.anime
}