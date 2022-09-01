package com.exam.themov

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.exam.themov.adapter.AnimeAdapter
import com.exam.themov.adapter.NowPlayingAdapter
import com.exam.themov.adapter.TrailerAdapter
import com.exam.themov.api.Request
import com.exam.themov.api.RetrofitHelper
import com.exam.themov.databinding.ActivityDetailBinding
import com.exam.themov.models.Anime.AnimeData
import com.exam.themov.models.video.VideoData
import com.exam.themov.repository.AnimeRepository
import com.exam.themov.viewmodels.MainViewModel
import com.exam.themov.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch
import java.lang.Math.random
import kotlin.random.Random

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    lateinit var mainViewModel: MainViewModel
    private lateinit var videoAdapter: TrailerAdapter
    val IMG_BASEURL = "https://image.tmdb.org/t/p/w500"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_detail)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val request = RetrofitHelper.getInstance().create(Request::class.java)
        val animeRepository = AnimeRepository(request)
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(animeRepository)
        ).get(MainViewModel::class.java)

        val videoId = intent.getIntExtra("id", 0)
        val genreIdList = intent.getIntegerArrayListExtra("genre_id")
        val movieId = intent.getIntExtra("movie_id", 0)

        var movieName = intent.getStringExtra("movieName").toString()
        var movieBackDrop = intent.getStringExtra("movieBackDrop").toString()

        var moviePoster = intent.getStringExtra("moviePoster").toString()
        var movieRating = intent.getDoubleExtra("Rating", 2.0)
        var moviePopularity = intent.getDoubleExtra("Popularity", 5.0)
        var movieOverview = intent.getStringExtra("Overview")

        Log.d("GenreList", "$genreIdList")

        val genreId: IntArray = intArrayOf(10759, 10765, 18, 9648, 14, 35, 80)
        val genreName = arrayOf(
            "Action",
            "Adventure",
            "Comedy",
            "Drama",
            "Horror",
            "Romance",
            "Girl",
            "Fantasy",
            "Western"
        )
        var showArrayList=ArrayList<String>()
        if (genreIdList != null) {
            for (i in 0 until 7){
                if (genreIdList.contains(genreId[i])){
                    showArrayList.add(genreName[i])
                }
            }

        }
        binding.tvGenre.text=showArrayList.toString()

        binding.tvTitleDetail.text = movieName
        binding.ivBackDrop.load(
            Uri.parse(IMG_BASEURL + movieBackDrop)
        ) {
            crossfade(1000)
            crossfade(true)
        }
        binding.ivPoster.load(
            Uri.parse(IMG_BASEURL + moviePoster)
        ) {
            crossfade(1000)
            crossfade(true)
        }
        binding.tvRating.text = movieRating.toString()
        binding.tvPopularity.text = moviePopularity.toString()
        binding.tvDetail.text = movieOverview

        binding.ibBackArrow.setOnClickListener {
            onBackPressed()
        }

        var videoData = MutableLiveData<VideoData>()
        //For Top-rated,Now playing and popular (movies)
        if (movieId != 0) {
            mainViewModel.viewModelScope.launch {

                var trailerVideo = mainViewModel.getTrailerForOther(movieId.toString())
                videoData.postValue(trailerVideo.body())
                Log.d("VideoResult", " ${trailerVideo.body()!!.results}")
                videoData.observe(this@DetailActivity) {
                    videoAdapter = TrailerAdapter(it.results)
                    binding.rvTrailers.apply {
                        setHasFixedSize(false)
                        layoutManager =
                            LinearLayoutManager(
                                this@DetailActivity,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                        adapter = videoAdapter
                    }
                }

            }
        }

        //for popular (tv series)
        if (videoId != 0) {
            mainViewModel.viewModelScope.launch {
                var trailerVideo = mainViewModel.getTrailerById(videoId.toString())
                videoData.postValue(trailerVideo.body())
                Log.d("VideoResult", " ${trailerVideo.body()!!.results}")
                videoData.observe(this@DetailActivity) {
                    videoAdapter = TrailerAdapter(it.results)
                    binding.rvTrailers.apply {
                        setHasFixedSize(false)
                        layoutManager =
                            LinearLayoutManager(
                                this@DetailActivity,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                        adapter = videoAdapter
                    }
                }
            }
        }

    }
}