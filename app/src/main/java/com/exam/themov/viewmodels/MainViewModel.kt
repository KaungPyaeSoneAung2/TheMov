package com.exam.themov.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exam.themov.models.Anime.AnimeData
import com.exam.themov.models.Anime.AnimeResult
import com.exam.themov.models.PopularData
import com.exam.themov.repository.PopularRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val popularRepository: PopularRepository): ViewModel() {

    init {

        viewModelScope.launch {
            popularRepository.getAnime()

        }
    }



    val anime : LiveData<AnimeData>
    get() = popularRepository.anime



    suspend fun getSearchResult(s:String): Response<AnimeData> {
        return popularRepository.getSearchResult(s)
    }

    suspend fun getAnimeByGenre(id:String):Response<AnimeData>{
        return popularRepository.getAnimeByGenre(id)
    }



}