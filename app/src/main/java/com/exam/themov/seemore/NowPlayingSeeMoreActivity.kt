package com.exam.themov.seemore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.exam.themov.MainActivity
import com.exam.themov.adapter.NowPlayingAdapter
import com.exam.themov.adapter.PopularAdapter
import com.exam.themov.api.Request
import com.exam.themov.api.RetrofitHelper
import com.exam.themov.databinding.ActivityNowPlayingSeeMoreBinding
import com.exam.themov.repository.AnimeRepository
import com.exam.themov.viewmodels.MainViewModel
import com.exam.themov.viewmodels.ViewModelFactory

class NowPlayingSeeMoreActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNowPlayingSeeMoreBinding
    private lateinit var nowPlayingAdapter: NowPlayingAdapter
    lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_now_playing_see_more)
        binding = ActivityNowPlayingSeeMoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val request = RetrofitHelper.getInstance().create(Request::class.java)

        val AnimeRepository = AnimeRepository(request)

        mainViewModel = ViewModelProvider(this, ViewModelFactory(AnimeRepository)
        ).get(MainViewModel::class.java)

        mainViewModel.nowPlayingAnime.observe(this) {

            nowPlayingAdapter = NowPlayingAdapter(it.results)
            binding.rvNowPlayingSM.also {
                it.setHasFixedSize(true)

                it.layoutManager = GridLayoutManager(this@NowPlayingSeeMoreActivity, 2)
                it.adapter = nowPlayingAdapter
            }

        }
        onClick()
    }
    private fun onClick(){
        binding.ibBack.setOnClickListener {
            var intent = Intent(this@NowPlayingSeeMoreActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}