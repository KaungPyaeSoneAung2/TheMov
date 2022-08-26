
package com.exam.themov


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.exam.themov.adapter.AnimeAdapter
import com.exam.themov.adapter.PopularAdapter
import com.exam.themov.adapter.SearchAdapter
import com.exam.themov.api.Request
import com.exam.themov.api.RetrofitHelper
import com.exam.themov.databinding.ActivityMainBinding
import com.exam.themov.models.Anime.AnimeData
import com.exam.themov.models.Anime.AnimeResult
import com.exam.themov.models.Result
import com.exam.themov.repository.PopularRepository
import com.exam.themov.viewmodels.MainViewModel
import com.exam.themov.viewmodels.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.internal.http2.Http2Reader
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var mainViewModel: MainViewModel
    private lateinit var popularAdapter: PopularAdapter
    private lateinit var animeAdapter: AnimeAdapter
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var request:Request

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val request = RetrofitHelper.getInstance().create(Request::class.java)


        val popularRepository = PopularRepository(request)



        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(popularRepository)
        ).get(MainViewModel::class.java)

//        mainViewModel.popular.observe(this){
//            Log.d("POPULAR", it.results.toString())
//            popularAdapter = PopularAdapter(it.results)
//            binding.recPopular.also {
//                it.setHasFixedSize(true)
//
//                it.layoutManager= LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
//                it.adapter = popularAdapter
//            }


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

            getImageSlide()

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

            Handler().postDelayed({ imageSlider.setImageList(imgList, ScaleTypes.FIT) }, 3000)
        }
    }

    private fun showSearchResult() {
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

            getImageSlide()
        }
    }

}
