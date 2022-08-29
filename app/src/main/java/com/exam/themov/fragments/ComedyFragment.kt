package com.exam.themov.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.exam.themov.R
import com.exam.themov.adapter.GenreAdapter
import com.exam.themov.api.Request
import com.exam.themov.api.RetrofitHelper
import com.exam.themov.databinding.FragmentAdventureBinding
import com.exam.themov.databinding.FragmentComedyBinding
import com.exam.themov.models.Anime.AnimeData
import com.exam.themov.repository.PopularRepository
import com.exam.themov.viewmodels.MainViewModel
import com.exam.themov.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch

class ComedyFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentComedyBinding

    var genre = MutableLiveData<AnimeData>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        // Inflate the layout for this fragment
        binding= FragmentComedyBinding.inflate(layoutInflater)

        val request = RetrofitHelper.getInstance().create(Request::class.java)
        val popularRepository = PopularRepository(request)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(popularRepository)
        ).get(MainViewModel::class.java)


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val genreData = viewModel.getAnimeByGenre("35")
            if (genreData.body() != null) {
                genre.postValue(genreData.body())
                Log.d("Genre", "onQueryTextChange: ${genreData.body()}")
                genre.observe(viewLifecycleOwner) {
                    Log.d("12", "onCreateView: ${it.results}")
                    val genreAdapter = GenreAdapter(it.results)
                    binding.rvComedy.apply {
                        adapter = genreAdapter
                        layoutManager =
                            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

                    }
                }
            }
        }

    }
}