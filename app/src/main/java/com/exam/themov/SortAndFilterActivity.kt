package com.exam.themov

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.viewpager.widget.ViewPager
import com.exam.themov.databinding.ActivitySortAndFilterBinding
import com.exam.themov.fragments.*
import com.exam.themov.adapter.FragmentAdapter
import com.exam.themov.adapter.GenreAdapter
import com.google.android.material.tabs.TabLayout

class SortAndFilterActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySortAndFilterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySortAndFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var viewPager = findViewById<ViewPager>(R.id.viewPager)
        var tabLayout = findViewById<TabLayout>(R.id.tabLayout)

        val fragmentAdapter = FragmentAdapter(supportFragmentManager)
        fragmentAdapter.addFragment(ActionFragment(),"Action")
        fragmentAdapter.addFragment(AdventureFragment(),"Adventure")
        fragmentAdapter.addFragment(ComedyFragment(),"Comedy")
        fragmentAdapter.addFragment(Fragment4(),"??")
        fragmentAdapter.addFragment(RomanceFragment(),"Romance")
        fragmentAdapter.addFragment(WesternFragment(),"Western")
        fragmentAdapter.addFragment(Fragment7(),"??")
        viewPager.adapter=fragmentAdapter
        tabLayout.setupWithViewPager(viewPager)

        binding.tvTotalResult.text=


        var spinnerAdapter= ArrayAdapter.createFromResource(this,R.array.Sort,android.R.layout.simple_spinner_item)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSort.adapter=spinnerAdapter


        var intent = Intent(this,GenreAdapter::class.java)
        var g=binding.spinnerSort.selectedItem.toString()
        Log.d("Hello How","This is spartaa $g")





    }



}
