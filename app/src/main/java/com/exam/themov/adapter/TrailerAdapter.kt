package com.exam.themov.adapter

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.exam.themov.databinding.ItemTrailerimageBinding
import com.exam.themov.models.video.VideoResult


class TrailerAdapter(
    val videoList: List<VideoResult>
): RecyclerView.Adapter<TrailerAdapter.TrailerHolder>() {
    val youtubeBaseUrl= "https://www.youtube.com/watch?v="
    inner class TrailerHolder(private val binding: ItemTrailerimageBinding) :RecyclerView.ViewHolder(binding.root){
        fun bind(videoList : VideoResult){
            val frontUrl = "https://i1.ytimg.com/vi/"
            val backUrl="/hqdefault.jpg"
            binding.ivTrailerImage.load(
                Uri.parse(frontUrl+ videoList.key+backUrl)
            ){
                crossfade(1000)
                crossfade(true)
            }
            Log.d("Nice", "bind: ${videoList.key}")

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerHolder {
        return TrailerHolder(
            ItemTrailerimageBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: TrailerHolder, position: Int) {
        holder.bind(videoList[position])
        holder.itemView.setOnClickListener{
            //val intent = Intent(it.context, DetailActivity::class.java)
            val intent=Intent(Intent.ACTION_VIEW,Uri.parse(youtubeBaseUrl+videoList[position].key))
            holder.itemView.context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return videoList.size
    }
}