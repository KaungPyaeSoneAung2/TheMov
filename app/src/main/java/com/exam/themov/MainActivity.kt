package com.exam.themov


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemChangeListener
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.exam.themov.adapter.AnimeAdapter
import com.exam.themov.adapter.NowPlayingAdapter
import com.exam.themov.adapter.PopularAdapter
import com.exam.themov.api.Request
import com.exam.themov.api.RetrofitHelper
import com.exam.themov.databinding.ActivityMainBinding
import com.exam.themov.models.Anime.AnimeData
import com.exam.themov.models.Anime.AnimeResult
import com.exam.themov.models.PopularData
import com.exam.themov.repository.AnimeRepository
import com.exam.themov.seemore.NowPlayingSeeMoreActivity
import com.exam.themov.seemore.PopularSeeMoreActivity
import com.exam.themov.seemore.TopRatedSeeMoreActivity
import com.exam.themov.seemore.UpComingActivity
import com.exam.themov.viewmodels.MainViewModel
import com.exam.themov.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var mainViewModel: MainViewModel
    private lateinit var popularAdapter: PopularAdapter
    private lateinit var nowPlayingAdapter: NowPlayingAdapter
    private lateinit var animeAdapter: AnimeAdapter
    var popularList = MutableLiveData<AnimeData>()
    var nowPlayingList = MutableLiveData<PopularData>()
    var topRatedList = MutableLiveData<PopularData>()
    var upComingList = MutableLiveData<PopularData>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val request = RetrofitHelper.getInstance().create(Request::class.java)
        val popularRepository = AnimeRepository(request)
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(popularRepository)
        ).get(MainViewModel::class.java)

        getImageSlide()
        getPopularAnime()
        getTopRatedAnime()
        getNowPlayingAnime()
        getUpcoming()
        onClick()

        binding.ivGenreAndSort.setOnClickListener {
            val intent = Intent(this, SortAndFilterActivity::class.java)
            startActivity(intent)
        }

        binding.searchView.setOnClickListener {
            Intent(this, SearchActivity::class.java).also { startActivity(it) }
        }
    }

    private fun getPopularAnime() {
        lifecycleScope.launch {
            val genreByPage = mainViewModel.getAnime(1)
            if (genreByPage.body() != null) {
                popularList.postValue(genreByPage.body())
                popularList.observe(this@MainActivity) {
                    animeAdapter = AnimeAdapter(it.results)
                    binding.rvPopular.also {
                        it.setHasFixedSize(true)
                        it.layoutManager = LinearLayoutManager(
                            this@MainActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        it.adapter = animeAdapter
                    }

                }
            }
        }
    }

    private fun getTopRatedAnime() {
        lifecycleScope.launch {
            val genreByPage = mainViewModel.getTopRatedAnime(1)
            if (genreByPage.body() != null) {
                topRatedList.postValue(genreByPage.body())
                topRatedList.observe(this@MainActivity) {
                    popularAdapter = PopularAdapter(it.results)
                    binding.rvTopRated.also {
                        it.setHasFixedSize(true)
                        it.layoutManager = LinearLayoutManager(
                            this@MainActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        it.adapter = popularAdapter
                    }

                }
            }
        }
    }

    private fun getNowPlayingAnime() {
        lifecycleScope.launch {
            val genreByPage = mainViewModel.getNowPlayingAnie(1)
            if (genreByPage.body() != null) {
                nowPlayingList.postValue(genreByPage.body())
                nowPlayingList.observe(this@MainActivity) {
                    nowPlayingAdapter = NowPlayingAdapter(it.results)
                    binding.rvNowPlaying.also {
                        it.setHasFixedSize(true)
                        it.layoutManager = LinearLayoutManager(
                            this@MainActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        it.adapter = nowPlayingAdapter
                    }

                }
            }
        }
    }

    private fun getUpcoming() {

        lifecycleScope.launch {
            val genreByPage = mainViewModel.getUpComing(1)
            if (genreByPage.body() != null) {
                upComingList.postValue(genreByPage.body())
                upComingList.observe(this@MainActivity) {
                    popularAdapter = PopularAdapter(it.results)
                    binding.rvUpcoming.also {
                        it.setHasFixedSize(true)
                        it.layoutManager = LinearLayoutManager(
                            this@MainActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        it.adapter = popularAdapter
                    }

                }
            }
        }
    }

    private fun getImageSlide() {
        val IMG_BASEURL = "https://image.tmdb.org/t/p/w500/"
        var imgList = ArrayList<SlideModel>()
        val fullList=ArrayList<AnimeData>()
        val resultList=ArrayList<AnimeResult>()
        mainViewModel.anime.observe(this) {
            for (i in 3 until 8 ) {
                resultList.add(
                    AnimeResult(
                        it.results.get(i).backdrop_path,
                        it.results.get(i).first_air_date,
                        it.results.get(i).genre_ids,
                        it.results.get(i).id,
                        it.results.get(i).name,
                        it.results.get(i).origin_country,
                        it.results.get(i).original_language,
                        it.results.get(i).original_name,
                        it.results.get(i).overview,
                        it.results.get(i).popularity,
                        it.results.get(i).poster_path,
                        it.results.get(i).vote_average,
                        it.results.get(i).vote_count
                    )
                )
                imgList.add(
                    SlideModel(
                        IMG_BASEURL + it.results.get(i).backdrop_path,
                        it.results.get(i).name
                    )
                )

            }
            val imageSlider = findViewById<ImageSlider>(R.id.imageSlider)
            imageSlider.setImageList(imgList, ScaleTypes.FIT)
            imageSlider.startSliding(3000)
            imageSlider.setItemClickListener(object : ItemClickListener {
                override fun onItemSelected(position: Int) {
                    val intent=Intent(this@MainActivity, DetailActivity::class.java)
                    intent.putIntegerArrayListExtra("genre_id",resultList[position].genre_ids as ArrayList<Int>)
                    intent.putExtra("id",resultList[position].id)
                    intent.putExtra("movieName",resultList[position].name)
                    intent.putExtra("movieBackDrop",resultList[position].backdrop_path)
                    intent.putExtra("moviePoster",resultList[position].poster_path)
                    intent.putExtra("Rating",resultList[position].vote_average)
                    intent.putExtra("Popularity",resultList[position].popularity)
                    intent.putExtra("Overview",resultList[position].overview)
                    startActivity(intent)
                }
            })
        }

    }

    private fun onClick() {
        binding.tvSeemore.setOnClickListener {
            var intent = Intent(this@MainActivity, PopularSeeMoreActivity::class.java)
            startActivity(intent)
        }
        binding.tvSeemore2.setOnClickListener {
            var intent = Intent(this@MainActivity, TopRatedSeeMoreActivity::class.java)
            startActivity(intent)
        }
        binding.tvSeemore3.setOnClickListener {
            var intent = Intent(this@MainActivity, NowPlayingSeeMoreActivity::class.java)
            startActivity(intent)
        }
        binding.tvSeemore4.setOnClickListener {
            var intent = Intent(this@MainActivity, UpComingActivity::class.java)
            startActivity(intent)
        }

    }


}
