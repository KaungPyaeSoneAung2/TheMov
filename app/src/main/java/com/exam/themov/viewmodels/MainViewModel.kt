package com.exam.themov.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exam.themov.models.Anime.AnimeData
import com.exam.themov.models.PopularData
import com.exam.themov.repository.AnimeRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val animeRepository: AnimeRepository): ViewModel() {

    init {

        viewModelScope.launch {
            animeRepository.getAnime()
            animeRepository.getTopRatedAnime()
            animeRepository.getNowPlayingAnime()
            animeRepository.getUpcomingAnime()
        }
    }



    val popularAnime : LiveData<AnimeData>
    get() = animeRepository.popularAnime

    val topRatedAnime : LiveData<PopularData>
    get() = animeRepository.topRated

    val nowPlayingAnime : LiveData<PopularData>
    get() = animeRepository.nowPlaying

    val upcomingAnime : LiveData<PopularData>
    get() = animeRepository.upcoming

    suspend fun getSearchResult(s:String): Response<AnimeData> {
        return animeRepository.getSearchResult(s)
    }

    suspend fun getAnimeByGenre(id:String):Response<AnimeData>{
        return animeRepository.getAnimeByGenre(id)
    }


}