package com.exam.themov.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exam.themov.models.Anime.AnimeData
import com.exam.themov.models.PopularData
import com.exam.themov.models.video.VideoData
import com.exam.themov.repository.AnimeRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val animeRepository: AnimeRepository): ViewModel() {

    init {

        viewModelScope.launch {
            animeRepository.dataForSlideShow()
            //animeRepository.getTopRatedAnime()
            //animeRepository.getUpComingAnime()
            //animeRepository.getNowPlayingAnime()
        }
    }


    val anime : LiveData<AnimeData>
    get() = animeRepository.anime

//    val topRatedAnime: LiveData<PopularData>
//    get() = animeRepository.topRated

//    val nowPlayingAnime : LiveData<PopularData>
//    get()=animeRepository.nowPlaying

    suspend fun getAnime(page: Int):Response<AnimeData>{
        return animeRepository.getAnime(page)
    }

    suspend fun getTopRatedAnime(page:Int):Response<PopularData> {
        return animeRepository.getTopRatedAnime(page)
    }

    suspend fun getUpComing(page:Int):Response<PopularData> {
        return animeRepository.getUpComingAnime(page)
    }

    suspend fun getNowPlayingAnie(page:Int):Response<PopularData> {
        return animeRepository.getNowPlayingAnime(page)
    }

    suspend fun getTrailerForOther(id:String):Response<VideoData>{
        return animeRepository.getTrailerForOther(id)
    }

    suspend fun  getTrailerById(id:String):Response<VideoData>{
        return animeRepository.getTrailerById(id)
    }
    suspend fun getSearchResult(s:String): Response<AnimeData> {
        return animeRepository.getSearchResult(s)
    }

    suspend fun getAnimeByGenre(id:String):Response<AnimeData>{
        return animeRepository.getAnimeByGenre(id)
    }

    suspend fun getAnimeByPage(page:Int,genreId:String):Response<AnimeData>{
        return animeRepository.getAnimeByPage(page,genreId)
    }

}