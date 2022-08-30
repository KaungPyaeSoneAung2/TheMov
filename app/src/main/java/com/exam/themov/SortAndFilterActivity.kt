package com.exam.themov

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.viewpager.widget.ViewPager
import com.exam.themov.databinding.ActivitySortAndFilterBinding
import com.exam.themov.fragments.*
import com.exam.themov.adapter.FragmentAdapter
import com.exam.themov.viewmodels.SortViewModel
import com.google.android.material.tabs.TabLayout

class SortAndFilterActivity : AppCompatActivity() ,AdapterView.OnItemSelectedListener{
    private lateinit var binding : ActivitySortAndFilterBinding
    private val viewModel:SortViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySortAndFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var viewPager = findViewById<ViewPager>(R.id.viewPager)
        var tabLayout = findViewById<TabLayout>(R.id.tabLayout)

        val fragmentAdapter = FragmentAdapter(supportFragmentManager)

        fragmentAdapter.addFragment(AdventureFragment(),"Adventure")
        fragmentAdapter.addFragment(ComedyFragment(),"Comedy")
        fragmentAdapter.addFragment(FantasyFragment(),"Fantasy")
        fragmentAdapter.addFragment(GirlAnimeFragment(),"Girl")
        fragmentAdapter.addFragment(SupernaturalFragment(),"Supernatural")
        fragmentAdapter.addFragment(WesternFragment(),"Western")
       // fragmentAdapter.addFragment(Fragment7(),"??")
        viewPager.adapter=fragmentAdapter
        tabLayout.setupWithViewPager(viewPager)



        var spinnerAdapter= ArrayAdapter.createFromResource(this,R.array.Sort,R.layout.selected_item)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
        binding.spinnerSort.adapter=spinnerAdapter
        binding.spinnerSort.onItemSelectedListener=this

        var g=binding.spinnerSort.selectedItem.toString()
        Log.d("Hello How","This is spartaa $g")

    }
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val text:String=p0?.getItemAtPosition(p2).toString()
        Toast.makeText(this, "$text", Toast.LENGTH_SHORT).show()
        viewModel.setSort(text)
        //intent.putExtra("selectedSpinner",text)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


}
