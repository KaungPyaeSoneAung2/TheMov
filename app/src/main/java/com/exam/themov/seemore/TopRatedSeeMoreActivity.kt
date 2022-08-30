package com.exam.themov.seemore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.exam.themov.MainActivity
import com.exam.themov.adapter.AnimeAdapter
import com.exam.themov.adapter.PopularAdapter
import com.exam.themov.api.Request
import com.exam.themov.api.RetrofitHelper
import com.exam.themov.databinding.ActivityTopRatedSeeMoreBinding
import com.exam.themov.repository.AnimeRepository
import com.exam.themov.viewmodels.MainViewModel
import com.exam.themov.viewmodels.ViewModelFactory

class TopRatedSeeMoreActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTopRatedSeeMoreBinding
    private lateinit var popularAdapter: PopularAdapter
    lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_top_rated_see_more)
        binding = ActivityTopRatedSeeMoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val request = RetrofitHelper.getInstance().create(Request::class.java)

        val AnimeRepository = AnimeRepository(request)

        mainViewModel = ViewModelProvider(this, ViewModelFactory(AnimeRepository)
        ).get(MainViewModel::class.java)

        mainViewModel.topRatedAnime.observe(this) {

            popularAdapter = PopularAdapter(it.results)
            binding.rvTopRatedSM.also {
                it.setHasFixedSize(true)

                it.layoutManager = GridLayoutManager(this@TopRatedSeeMoreActivity, 2)
                it.adapter = popularAdapter
            }
        onClick()
        }

    }private fun onClick(){
        binding.ibBack.setOnClickListener {
            var intent = Intent(this@TopRatedSeeMoreActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}