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

class AnimeAdapter(
    val animeList: List<AnimeResult>
): RecyclerView.Adapter<AnimeAdapter.AnimeHolder>() {

    inner class AnimeHolder(private val binding: PopularMoviesBinding) :RecyclerView.ViewHolder(binding.root){
        fun bind(animeList : AnimeResult){
            val IMG_BASEURL = "https://image.tmdb.org/t/p/w500/"
            binding.ivMovImg.load(
                Uri.parse(IMG_BASEURL+animeList.poster_path)
            ){
                crossfade(1000)
                crossfade(true)
            }
            binding.tvTitle.text=animeList.name
            binding.tvLanguage.text=animeList.original_language
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnimeAdapter.AnimeHolder {
        return AnimeHolder(
            PopularMoviesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: AnimeAdapter.AnimeHolder, position: Int) {
        holder.bind(animeList[position])
    }

    override fun getItemCount(): Int {
        return animeList.size
    }


}