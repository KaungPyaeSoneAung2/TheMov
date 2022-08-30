package com.exam.themov.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.exam.themov.DetailActivity
import com.exam.themov.databinding.AnimeItemBinding
import com.exam.themov.databinding.TestActivityBinding
import com.exam.themov.models.Anime.AnimeResult
import com.exam.themov.models.Result

class NowPlayingAdapter(
    val popularList: List<Result>
): RecyclerView.Adapter<NowPlayingAdapter.NowPlayingHolder>() {

    inner class NowPlayingHolder(private val binding: TestActivityBinding) :RecyclerView.ViewHolder(binding.root){
        fun bind(nowPlayingList : Result){
            val IMG_BASEURL = "https://image.tmdb.org/t/p/w500/"
            binding.ivMovImg.load(
                Uri.parse(IMG_BASEURL+ nowPlayingList.poster_path)
            ){
                crossfade(1000)
                crossfade(true)

            }
            binding.tvTitle.text= nowPlayingList.title

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NowPlayingHolder {
        return NowPlayingHolder(
            TestActivityBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: NowPlayingHolder, position: Int) {
        holder.bind(popularList[position])
        holder.itemView.setOnClickListener{
            val intent = Intent(it.context, DetailActivity::class.java)
            intent.putExtra("movieName",popularList[position].title)
            intent.putExtra("movieBackDrop",popularList[position].backdrop_path)
            intent.putExtra("moviePoster",popularList[position].poster_path)
            intent.putExtra("Rating",popularList[position].vote_average)
            intent.putExtra("Popularity",popularList[position].popularity)
            intent.putExtra("Overview",popularList[position].overview)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return popularList.size
    }
}