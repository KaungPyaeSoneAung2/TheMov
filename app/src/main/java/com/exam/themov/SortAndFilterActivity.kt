package com.exam.themov

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.exam.themov.fragments.*
import com.exam.themov.fragments.fgadapter.FragmentAdapter
import com.google.android.material.tabs.TabLayout

class SortAndFilterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sort_and_filter)

        var viewPager = findViewById<ViewPager>(R.id.viewPager)
        var tabLayout = findViewById<TabLayout>(R.id.tabLayout)

        val fragmentAdapter = FragmentAdapter(supportFragmentManager)
        fragmentAdapter.addFragment(ActionFragment(),"Action")
        fragmentAdapter.addFragment(AdventureFragment(),"Funny")
        fragmentAdapter.addFragment(ComedyFragment(),"Drama")
        fragmentAdapter.addFragment(Fragment4(),"Drama")
        fragmentAdapter.addFragment(Fragment5(),"Drama")
        fragmentAdapter.addFragment(Fragment6(),"Drama")
        fragmentAdapter.addFragment(Fragment7(),"Drama")

        viewPager.adapter=fragmentAdapter
        tabLayout.setupWithViewPager(viewPager)


    }
}