package com.exam.themov

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.exam.themov.adapter.AnimeAdapter
import com.exam.themov.api.Request
import com.exam.themov.api.RetrofitHelper
import com.exam.themov.databinding.ActivitySearchBinding
import com.exam.themov.models.Anime.AnimeData
import com.exam.themov.repository.AnimeRepository
import com.exam.themov.viewmodels.MainViewModel
import com.exam.themov.viewmodels.ViewModelFactory
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySearchBinding
    lateinit var mainViewModel: MainViewModel
    lateinit var animeAdapter:AnimeAdapter
    var popularList = MutableLiveData<AnimeData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val request = RetrofitHelper.getInstance().create(Request::class.java)
        val popularRepository = AnimeRepository(request)
        mainViewModel = ViewModelProvider(this, ViewModelFactory(popularRepository)).get(MainViewModel::class.java)

        getPopularAnime()
        var searchData = MutableLiveData<AnimeData>()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                if (p0 != null) {
                    if (p0 == "") {
                        binding.rvSearchResult.visibility = View.INVISIBLE
                    }
                    else {
                        binding.rvSearchResult.visibility = View.VISIBLE
                        mainViewModel.viewModelScope.launch {
                            var searchResult = mainViewModel.getSearchResult(p0.toString())
                            if (searchResult.body() != null) {
                                searchData.postValue(searchResult.body())
                                Log.d("SearchResult", "onQueryTextChange: ${searchResult.body()}")
                                searchData.observe(this@SearchActivity) {
                                    animeAdapter = AnimeAdapter(it.results)
                                    binding.rvSearchResult.also {
                                        it.setHasFixedSize(true)
                                        it.layoutManager = GridLayoutManager(this@SearchActivity, 2)
                                        it.adapter = animeAdapter
                                    }
                                }
                            } else {
                                Toast.makeText(this@SearchActivity, "Can't Search", Toast.LENGTH_SHORT)
                                    .show()
                                Log.d("SearchResult", "onQueryTextChange: ${searchResult.body()}")
                            }
                        }

                    }
                }
                return true
                }
        })
        binding.ivGenreAndSort.setOnClickListener {
            val intent= Intent(this,SortAndFilterActivity::class.java)
            startActivity(intent)
        }
        binding.ivBackButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getPopularAnime(){
        lifecycleScope.launch {
            val genreByPage = mainViewModel.getAnime(1)
            if (genreByPage.body() != null) {
                popularList.postValue(genreByPage.body())
                popularList.observe(this@SearchActivity) {
                    animeAdapter = AnimeAdapter(it.results)
                    binding.rvSearchResult.also {
                        it.setHasFixedSize(true)
                        it.layoutManager = GridLayoutManager(this@SearchActivity, 2)
                        it.adapter = animeAdapter
                    }

                }

            }
        }
    }
}