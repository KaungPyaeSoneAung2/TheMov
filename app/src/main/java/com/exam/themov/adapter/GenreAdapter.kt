package com.exam.themov.adapter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.exam.themov.DetailActivity
import com.exam.themov.SortAndFilterActivity
import com.exam.themov.databinding.GenreItemsBinding
import com.exam.themov.models.Anime.AnimeResult


class GenreAdapter(
    val genreList: List<AnimeResult>,var sortList : String
): RecyclerView.Adapter<GenreAdapter.GenreHolder>() {


    inner class GenreHolder(private val binding: GenreItemsBinding) :RecyclerView.ViewHolder(binding.root){
        fun bind(genreList : AnimeResult){

            val IMG_BASEURL = "https://image.tmdb.org/t/p/w500/"
            binding.ivMovImg.load(
                Uri.parse(IMG_BASEURL+genreList.poster_path)
            ){
                crossfade(1000)
                crossfade(true)

            }


            binding.tvTitle.text = genreList.name
            binding.tvOverview.text = genreList.overview
            Log.d("AdapterBind","bind: ${binding.tvTitle.text}")

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreAdapter.GenreHolder {
        return GenreHolder(
            GenreItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )

        Log.d("AdapterBind","bind: Konichiwa")
    }

    override fun onBindViewHolder(holder: GenreAdapter.GenreHolder, position: Int) {
        var sortedList : List<AnimeResult> = genreList
        when(sortList){
            "By Name" -> sortedList = genreList.sortedBy { it.name }
            "By Rating(Ascending)" -> sortedList = genreList.sortedBy { it.vote_average }
            "By Rating(Descending)" -> sortedList = genreList.sortedByDescending { it.vote_average }
            "Popularity(Ascending)" -> sortedList = genreList.sortedBy { it.popularity }
            "Popularity(Descending)" -> sortedList = genreList.sortedByDescending { it.popularity}
        }

        holder.bind(sortedList[position])
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, DetailActivity::class.java)
            intent.putIntegerArrayListExtra("genre_id",sortedList[position].genre_ids as ArrayList<Int>)
            intent.putExtra("id",sortedList[position].id)
            intent.putExtra("movieName",sortedList[position].name)
            intent.putExtra("movieBackDrop",sortedList[position].backdrop_path)
            intent.putExtra("moviePoster",sortedList[position].poster_path)
            intent.putExtra("Rating",sortedList[position].vote_average)
            intent.putExtra("Popularity",sortedList[position].popularity)
            intent.putExtra("Overview",sortedList[position].overview)
            it.context.startActivity(intent)
        }

//        val selectedItem = intent.getStringExtra("spinnerSelected").toString()
//        Log.d("GLGLGL","The result :$selectedItem")

    }

    override fun getItemCount(): Int {
        return genreList.size
    }

}