package com.exam.themov.seemore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.exam.themov.MainActivity
import com.exam.themov.adapter.PopularAdapter
import com.exam.themov.api.Request
import com.exam.themov.api.RetrofitHelper
import com.exam.themov.databinding.ActivityUpComingBinding
import com.exam.themov.repository.AnimeRepository
import com.exam.themov.viewmodels.MainViewModel
import com.exam.themov.viewmodels.ViewModelFactory

class UpComingActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUpComingBinding
    private lateinit var popularAdapter: PopularAdapter
    lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_up_coming)
        binding = ActivityUpComingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val request = RetrofitHelper.getInstance().create(Request::class.java)

        val AnimeRepository = AnimeRepository(request)

        mainViewModel = ViewModelProvider(this, ViewModelFactory(AnimeRepository)
        ).get(MainViewModel::class.java)

        mainViewModel.upComingAnime.observe(this) {

            popularAdapter = PopularAdapter(it.results)
            binding.rvUpcomingSM.also {
                it.setHasFixedSize(true)

                it.layoutManager = GridLayoutManager(this@UpComingActivity, 2)
                it.adapter = popularAdapter
            }

        }
        onClick()
    }
    private fun onClick(){
        binding.ibBack.setOnClickListener {
            var intent = Intent(this@UpComingActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}