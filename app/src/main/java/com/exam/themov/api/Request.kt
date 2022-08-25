package com.exam.themov.api

import com.exam.themov.models.Anime.AnimeData
import com.exam.themov.models.PopularData
import retrofit2.Response
import retrofit2.http.GET

interface Request {

    @GET("discover/tv?api_key=bbf5a3000e95f1dddf266b5e187d4b21&language=en-US&page=1&with_genres=16&with_keywords=210024%7C287501")
    //@GET("movie/popular?api_key=4dc21da43cf0589415156e4904279d08")
    suspend fun getAnime() : Response<AnimeData>
}