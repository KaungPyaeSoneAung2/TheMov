package com.exam.themov.repository

import android.util.Log
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.exam.themov.api.Request
import com.exam.themov.models.Anime.AnimeData
import com.exam.themov.models.PopularData
import com.exam.themov.models.video.VideoData
import retrofit2.Response

class AnimeRepository(private val request: Request) {

    private val animeLiveData = MutableLiveData<AnimeData>()

        suspend fun getAnime(id:Int):Response<AnimeData>{

                return request.getAnime(id)
        }

    val anime : LiveData<AnimeData>
    get() = animeLiveData

    suspend fun dataForSlideShow(){
        val result = request.getAnime(1)
        if(result.body()!= null){
            animeLiveData.postValue(result.body())
        }
    }

    suspend fun getTrailerById(id : String):Response<VideoData>{
        return request.getTrailerById(id)
    }

    suspend fun getTrailerForOther(id:String):Response<VideoData>{
        return request.getTrailerForOthers(id)
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

    suspend fun getTopRatedAnime(page:Int):Response<PopularData>{
        return request.getTopRatedAnime(page)
    }
    suspend fun getNowPlayingAnime(page:Int):Response<PopularData>{
        return request.getNowPlayingAnime(page)
    }

    suspend fun getUpComingAnime(page:Int):Response<PopularData>{
        return request.getUpcomingAnime(page)
    }
}