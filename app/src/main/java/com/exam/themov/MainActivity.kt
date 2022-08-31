
package com.exam.themov


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.exam.themov.adapter.AnimeAdapter
import com.exam.themov.adapter.NowPlayingAdapter
import com.exam.themov.adapter.PopularAdapter
import com.exam.themov.api.Request
import com.exam.themov.api.RetrofitHelper
import com.exam.themov.databinding.ActivityMainBinding
import com.exam.themov.models.Anime.AnimeData
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
    private lateinit var  nowPlayingAdapter: NowPlayingAdapter
    private lateinit var animeAdapter: AnimeAdapter
    var nowPlayingList = MutableLiveData<PopularData>()
    var topRatedList = MutableLiveData<PopularData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val request = RetrofitHelper.getInstance().create(Request::class.java)
        val popularRepository = AnimeRepository(request)
        mainViewModel = ViewModelProvider(this, ViewModelFactory(popularRepository)).get(MainViewModel::class.java)

        getImageSlide()
        getPopularAnime()
        getTopRatedAnime()
        getNowPlayingAnime()
        getUpcoming()
        onClick()

        binding.ivGenreAndSort.setOnClickListener {
            val intent=Intent(this,SortAndFilterActivity::class.java)
                startActivity(intent)
        }

        var searchView = findViewById<SearchView>(R.id.searchView)
        var searchData = MutableLiveData<AnimeData>()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                mainViewModel.viewModelScope.launch {
                    var searchResult = mainViewModel.getSearchResult(p0.toString())
                    if (searchResult.body()!= null){
                        searchData.postValue(searchResult.body())
                        Log.d("SearchResult", "onQueryTextChange: ${searchResult.body()}")
                        searchData.observe(this@MainActivity){
                            animeAdapter = AnimeAdapter(it.results)
                            binding.recPopular.also {
                                it.setHasFixedSize(true)

                                it.layoutManager =
                                    LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                                it.adapter = animeAdapter
                            }
                        }
                    }
                    else{
                        Toast.makeText(this@MainActivity, "Can't Search", Toast.LENGTH_SHORT).show()
                        Log.d("SearchResult", "onQueryTextChange: ${searchResult.body()}")
                    }
                }


                return true
            }


        })


    }

    private fun getPopularAnime(){
        mainViewModel.anime.observe(this) {
            Log.d("POPULAR", it.results.toString())
            Log.d("Search", "onCreate: ${it.results}")
            animeAdapter = AnimeAdapter(it.results)
            binding.recPopular.also {
                it.setHasFixedSize(true)

                it.layoutManager =
                    LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                it.adapter = animeAdapter
            }


        }
    }

//    private fun getTopRatedAnime(){
//        mainViewModel.topRatedAnime.observe(this){
//            popularAdapter = PopularAdapter(it.results)
//            binding.rvTopRated.apply {
//                setHasFixedSize(true)
//
//                layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
//                adapter = popularAdapter
//            }
//        }
//    }
    private fun getTopRatedAnime(){
    lifecycleScope.launch {
        val genreByPage = mainViewModel.getTopRatedAnime(1)
        if (genreByPage.body() != null) {
            topRatedList.postValue(genreByPage.body())
            topRatedList.observe(this@MainActivity) {
                popularAdapter = PopularAdapter(it.results)
                binding.rvTopRated.also {
                    it.setHasFixedSize(true)
                    it.layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
                    it.adapter = popularAdapter
                }

            }
        }
    }
    }

    private fun getNowPlayingAnime(){
        lifecycleScope.launch {
        val genreByPage = mainViewModel.getNowPlayingAnie(1)
        if (genreByPage.body() != null) {
            nowPlayingList.postValue(genreByPage.body())
            nowPlayingList.observe(this@MainActivity) {
                nowPlayingAdapter = NowPlayingAdapter(it.results)
                binding.rvNowPlaying.also {
                    it.setHasFixedSize(true)
                    it.layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
                    it.adapter = nowPlayingAdapter
                }

            }
        }
    }
    }

    private fun getUpcoming(){
        mainViewModel.upComingAnime.observe(this){
            popularAdapter = PopularAdapter(it.results)
            binding.rvUpcoming.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
                adapter = popularAdapter
            }
        }
    }

    private fun getImageSlide() {
        val IMG_BASEURL = "https://image.tmdb.org/t/p/w500/"
        var imgList = ArrayList<SlideModel>()

        mainViewModel.anime.observe(this) {
            for (i in 0 until 5) {
                imgList.add(
                    SlideModel(
                        IMG_BASEURL + it.results.get(i).backdrop_path,
                        it.results.get(i).name
                    )
                )

            }
            val imageSlider = findViewById<ImageSlider>(R.id.imageSlider)


            Handler().postDelayed({ imageSlider.setImageList(imgList, ScaleTypes.FIT) }, 2000)
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
