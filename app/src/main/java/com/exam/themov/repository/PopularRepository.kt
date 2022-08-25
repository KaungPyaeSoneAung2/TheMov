package com.exam.themov.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.exam.themov.api.Request
import com.exam.themov.models.Anime.AnimeData
import com.exam.themov.models.PopularData

class PopularRepository(private val request: Request) {

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

        val anime : LiveData<AnimeData>
            get() = animeLiveData

        suspend fun getAnime(){
            val result = request.getAnime()

            if (result.body()!=null){
                animeLiveData.postValue(result.body())
                Log.d("POPULAR", "getPopular: ${result.body()}")
            }
        }

        suspend fun getSearchResult(searchText:String){
            val result = request.getSearchResult(searchText)

            if(result.body()!=null){
                animeLiveData.postValue(result.body())
                Log.d("Search", "getSearchResult: ${result.body()} ")
            }
        }
}