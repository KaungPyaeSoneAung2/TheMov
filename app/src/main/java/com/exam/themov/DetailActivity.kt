package com.exam.themov

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import coil.load
import com.exam.themov.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    val IMG_BASEURL = "https://image.tmdb.org/t/p/w500"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_detail)
        binding= ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var movieName= intent.getStringExtra("movieName").toString()
        var movieBackDrop = intent.getStringExtra("movieBackDrop").toString()
        Log.d("TAG", "onCreate: ${movieBackDrop}")
        var moviePoster = intent.getStringExtra("moviePoster").toString()
        var movieRating = intent.getDoubleExtra("Rating",2.0)
        var moviePopularity = intent.getDoubleExtra("Popularity",5.0)
        var movieOverview = intent.getStringExtra("Overview")


        binding.tvTitleDetail.text=movieName
        binding.ivBackDrop.load(
            Uri.parse(IMG_BASEURL+movieBackDrop)
        ){
            crossfade(1000)
            crossfade(true)
        }
        binding.ivPoster.load(
            Uri.parse(IMG_BASEURL+moviePoster)
        ){
            crossfade(1000)
            crossfade(true)
        }
        binding.tvRating.text= movieRating.toString()
        binding.tvPopularity.text= moviePopularity.toString()
        binding.tvDetail.text= movieOverview

        binding.ibBackArrow.setOnClickListener {
            var intent = Intent(this@DetailActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}