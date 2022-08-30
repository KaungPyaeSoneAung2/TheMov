package com.exam.themov.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exam.themov.adapter.GenreAdapter
import com.exam.themov.api.Request
import com.exam.themov.api.RetrofitHelper
import com.exam.themov.databinding.FragmentComedyBinding
import com.exam.themov.models.Anime.AnimeData
import com.exam.themov.repository.AnimeRepository
import com.exam.themov.viewmodels.MainViewModel
import com.exam.themov.viewmodels.SortViewModel
import com.exam.themov.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch

class ComedyFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentComedyBinding
    private val sortViewModel: SortViewModel by activityViewModels()
    var genre = MutableLiveData<AnimeData>()
    var sortBy = ""
    var totalPage = 0
    var currentPage = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        // Inflate the layout for this fragment
        binding= FragmentComedyBinding.inflate(layoutInflater)

        val request = RetrofitHelper.getInstance().create(Request::class.java)
        val popularRepository = AnimeRepository(request)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(popularRepository)
        ).get(MainViewModel::class.java)

        sortViewModel.sortValue.observe(viewLifecycleOwner){
            sortBy = it.toString()
            GoNext(currentPage)
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GoNext(currentPage)

        detectLastItemOfRv()
        binding.ivBackPage.visibility = View.GONE
        // binding.ivBackPage.visibility=View.VISIBLE
        binding.ivForwardPage.setOnClickListener{
            GoNext(++currentPage)
            binding.ivBackPage.visibility = View.VISIBLE
            Log.d("totalPage", "TotalPage : ${totalPage}")
            if(currentPage >= totalPage){
                binding.ivForwardPage.visibility = View.GONE
            }

        }
        binding.ivBackPage.setOnClickListener{
            GoNext(--currentPage)
            if(currentPage <= 1){
                binding.ivBackPage.visibility = View.GONE
            }
        }

    }

        private fun detectLastItemOfRv() {
            binding.rvComedy.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val layoutManager = LinearLayoutManager::class.java.cast(recyclerView.layoutManager)
                    val totalItemCount = layoutManager.itemCount
                    val lastVisible = layoutManager.findLastVisibleItemPosition()
                    val endHasBeenReached = lastVisible + 5 >= totalItemCount
                    if (totalItemCount > 0 && endHasBeenReached) {
                        //you have reached to the bottom of your recycler view
                        binding.llPageControl.visibility = View.VISIBLE
                    } else {
                        binding.llPageControl.visibility = View.GONE
                    }
                }
            })

        }

        private fun GoNext(currentPage:Int){
            lifecycleScope.launch {
                val genreByPage = viewModel.getAnimeByPage(currentPage,"35")
                if (genreByPage.body() != null) {
                    totalPage = genreByPage.body()!!.total_pages
                    genre.postValue(genreByPage.body())
                    Log.d("Genre", "onQueryTextChange: ${genreByPage.body()}")
                    genre.observe(viewLifecycleOwner) {
                        Log.d("12", "onCreateView: ${it.results}")
                        val genreAdapter = GenreAdapter(it.results,sortBy)
                        binding.rvComedy.apply {
                            Log.d("RvBind", "onCreateView: ${genreAdapter}")
                            adapter = genreAdapter
                            layoutManager =
                                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                        }
                    }
                }
            }
        }

}