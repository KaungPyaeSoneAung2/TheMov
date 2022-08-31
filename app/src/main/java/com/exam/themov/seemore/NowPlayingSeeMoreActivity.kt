package com.exam.themov.seemore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.exam.themov.MainActivity
import com.exam.themov.adapter.GenreAdapter
import com.exam.themov.adapter.NowPlayingAdapter
import com.exam.themov.adapter.PopularAdapter
import com.exam.themov.api.Request
import com.exam.themov.api.RetrofitHelper
import com.exam.themov.databinding.ActivityNowPlayingSeeMoreBinding
import com.exam.themov.models.Anime.AnimeData
import com.exam.themov.models.PopularData
import com.exam.themov.repository.AnimeRepository
import com.exam.themov.viewmodels.MainViewModel
import com.exam.themov.viewmodels.SortViewModel
import com.exam.themov.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch

class NowPlayingSeeMoreActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNowPlayingSeeMoreBinding
    private lateinit var nowPlayingAdapter: NowPlayingAdapter
    private lateinit var viewModel: MainViewModel

    var currentPage=1
    var totalPage=0
    var nowPlayingList = MutableLiveData<PopularData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_now_playing_see_more)
        binding = ActivityNowPlayingSeeMoreBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val request = RetrofitHelper.getInstance().create(Request::class.java)

        val AnimeRepository = AnimeRepository(request)

        viewModel = ViewModelProvider(this, ViewModelFactory(AnimeRepository)
        ).get(MainViewModel::class.java)

        GoNext(currentPage)
//
//        mainViewModel.nowPlayingAnime.observe(this) {
//
//            nowPlayingAdapter = NowPlayingAdapter(it.results)
//            binding.rvNowPlayingSM.also {
//                it.setHasFixedSize(true)
//
//                it.layoutManager = GridLayoutManager(this@NowPlayingSeeMoreActivity, 2)
//                it.adapter = nowPlayingAdapter
//            }
//
//        }



        onClick()
    }
    private fun onClick(){
        binding.ibBack.setOnClickListener {
            onBackPressed()
        }
        binding.ibPageFront.setOnClickListener {

            //here comes the next page function
            GoNext(++currentPage)
            if(currentPage >=totalPage){
                binding.ibPageFront.visibility= View.INVISIBLE
                binding.ibPageFront.isClickable=false
            }
            binding.ibPageBack.visibility=View.VISIBLE
            binding.ibPageBack.isClickable=true
        }
        binding.ibPageBack.setOnClickListener {
            //here is the page back function
            GoNext(--currentPage)
            if(currentPage <=1){
                binding.ibPageBack.visibility= View.INVISIBLE
                binding.ibPageBack.isClickable=false
            }
                binding.ibPageFront.visibility= View.VISIBLE
                binding.ibPageFront.isClickable=true

        }
    }

    private fun GoNext(currentPage:Int){
        lifecycleScope.launch {
            val nowPlayingListByPage = viewModel.getNowPlayingAnie(currentPage)
            if (nowPlayingListByPage.body() != null) {
                totalPage = nowPlayingListByPage.body()!!.total_pages
                nowPlayingList.postValue(nowPlayingListByPage.body())
//                genre.observe(viewLifecycleOwner) {
//                    Log.d("12", "onCreateView: ${it.results}")
//                    val genreAdapter = GenreAdapter(it.results,sortBy)
//                    binding.rvAdventure.apply {
//                        Log.d("RvBind", "onCreateView: ${genreAdapter}")
//                        adapter = genreAdapter
//                        layoutManager =
//                            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
//                    }
//                }
                nowPlayingList.observe(this@NowPlayingSeeMoreActivity) {

                    nowPlayingAdapter = NowPlayingAdapter(it.results)
                    binding.rvNowPlayingSM.also {
                        it.setHasFixedSize(true)

                        it.layoutManager = GridLayoutManager(this@NowPlayingSeeMoreActivity, 2)
                        it.adapter = nowPlayingAdapter
                    }

                }
            }
        }
    }

}