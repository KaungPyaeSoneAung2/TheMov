package com.exam.themov.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.exam.themov.databinding.PopularMoviesBinding
import com.exam.themov.models.Anime.AnimeResult
import com.exam.themov.models.Result

class SearchAdapter(
    val searchList: List<AnimeResult>
): RecyclerView.Adapter<SearchAdapter.SearchHolder>() {

    inner class SearchHolder(private val binding: PopularMoviesBinding) :RecyclerView.ViewHolder(binding.root){
        fun bind(SearchList : AnimeResult){
            val IMG_BASEURL = "https://image.tmdb.org/t/p/w500/"
            binding.ivMovImg.load(
                Uri.parse(IMG_BASEURL+SearchList.poster_path)
            ){
                crossfade(1000)
                crossfade(true)

            }
            binding.tvTitle.text=SearchList.name.toString()
            binding.tvLanguage.text=SearchList.original_language.toString()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchAdapter.SearchHolder {
        return SearchHolder(
            PopularMoviesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: SearchAdapter.SearchHolder, position: Int) {
        holder.bind(searchList[position])
    }

    override fun getItemCount(): Int {
        return searchList.size
    }


}