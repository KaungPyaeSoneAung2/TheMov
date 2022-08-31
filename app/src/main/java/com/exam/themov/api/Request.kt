package com.exam.themov.api

import com.exam.themov.models.Anime.AnimeData
import com.exam.themov.models.Anime.AnimeResult
import com.exam.themov.models.PopularData
import com.exam.themov.models.video.VideoData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Request {
    //@GET("movie/popular?api_key=4dc21da43cf0589415156e4904279d08")


    @GET("discover/tv?api_key=bbf5a3000e95f1dddf266b5e187d4b21&language=en-US&with_genres=16&with_keywords=210024%7C287501")
    suspend fun getAnime(@Query("page")pageNum : Int) : Response<AnimeData>

    @GET("movie/top_rated?api_key=bbf5a3000e95f1dddf266b5e187d4b21&language=en-US&with_genres=16&with_keywords=210024|287501")
    suspend fun getTopRatedAnime(@Query("page")pageNum : Int) : Response<PopularData>

    @GET("movie/now_playing?api_key=bbf5a3000e95f1dddf266b5e187d4b21&language=en-US&with_genres=16&with_keywords=210024|287501")
    suspend fun getNowPlayingAnime(@Query("page")pageNum : Int) : Response<PopularData>

    @GET("movie/upcoming?api_key=bbf5a3000e95f1dddf266b5e187d4b21&language=en-US&with_genres=16&with_keywords=210024|287501")
    suspend fun getUpcomingAnime(@Query("page")pageNum : Int) : Response<PopularData>

    @GET("discover/tv?api_key=bbf5a3000e95f1dddf266b5e187d4b21&language=en-US&with_genres=16&with_keywords=210024|287501")
    suspend fun getSearchResult(@Query("with_text_query")searchText : String): Response<AnimeData>

    @GET("discover/tv?api_key=bbf5a3000e95f1dddf266b5e187d4b21&language=en-US&with_keywords=210024|287501")
    suspend fun getAnimeByGenre(@Query("with_genres") genereId: String) : Response<AnimeData>

    @GET("discover/tv?api_key=bbf5a3000e95f1dddf266b5e187d4b21&language=en-US&with_keywords=210024|287501")
    suspend fun getAnimeByPage(@Query("page")pageNum : Int,@Query("with_genres") genereId: String) : Response<AnimeData>

    @GET("tv/{tv_id}/videos?api_key=bbf5a3000e95f1dddf266b5e187d4b21")
    suspend fun getTrailerById(@Path("tv_id") id: String) : Response<VideoData>

    @GET("movie/{movie_id}/videos?api_key=bbf5a3000e95f1dddf266b5e187d4b21")
    suspend fun getTrailerForOthers(@Path("movie_id") id : String) : Response<VideoData>

}