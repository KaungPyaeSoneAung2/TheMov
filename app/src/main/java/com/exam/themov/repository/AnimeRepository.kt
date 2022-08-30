package com.exam.themov.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.exam.themov.api.Request
import com.exam.themov.models.Anime.AnimeData
import com.exam.themov.models.PopularData
import retrofit2.Response

class AnimeRepository(private val request: Request) {

//    private val popularLiveData = MutableLiveData<PopularData>()
//
//    val popular : LiveData<PopularData>
//        get() = popularLiveData
//
//    suspend fun getPopular(){
//        val result = request.getPopular()
//
//        if (result.body()!=null){
//            popularLiveData.postValue(result.body())
//            Log.d("POPULAR", "getPopular: ${result.body()}")
//        }
//    }
    private val animeLiveData = MutableLiveData<AnimeData>()
    private val topRatedAnime=MutableLiveData<PopularData>()
    private val nowPlayingAnime= MutableLiveData<PopularData>()
    private val upComingAnime =MutableLiveData<PopularData>()

        val anime : LiveData<AnimeData>
            get() = animeLiveData

        suspend fun getAnime(){
            val result = request.getAnime()

            if (result.body()!=null){
                animeLiveData.postValue(result.body())
                Log.d("POPULAR", "getPopular: ${result.body()}")
            }
        }

    suspend fun getSearchResult(s: String): Response<AnimeData> {
        return request.getSearchResult(s)
    }

    suspend fun getAnimeByGenre(id: String): Response<AnimeData> {
        return request.getAnimeByGenre(id)
    }

    suspend fun getAnimeByPage(page:Int,genreId:String) : Response<AnimeData>{
        return request.getAnimeByPage(page,genreId)
    }

    val topRated:LiveData<PopularData>
    get()=topRatedAnime

    suspend fun getTopRatedAnime(){
        val result = request.getTopRatedAnime()

        if (result.body()!=null){
            topRatedAnime.postValue(result.body())
            Log.d("POPULAR", "getPopular: ${result.body()}")
        }
    }

    val nowPlaying:LiveData<PopularData>
    get()=nowPlayingAnime

    suspend fun getNowPlayingAnime(){
        val result = request.getNowPlayingAnime()

        if (result.body()!=null){
            nowPlayingAnime.postValue(result.body())
            Log.d("POPULAR", "getPopular: ${result.body()}")
        }
    }

    val upComing:LiveData<PopularData>
    get()=upComingAnime

    suspend fun getUpComingAnime(){
        val result = request.getUpcomingAnime()

        if (result.body()!=null){
            upComingAnime.postValue(result.body())
            Log.d("POPULAR", "getPopular: ${result.body()}")
        }
    }
}