package com.exam.themov

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.viewpager.widget.ViewPager
import com.exam.themov.databinding.ActivitySortAndFilterBinding
import com.exam.themov.fragments.*
import com.exam.themov.adapter.FragmentAdapter
import com.exam.themov.api.Request
import com.exam.themov.api.RetrofitHelper
import com.exam.themov.repository.AnimeRepository
import com.exam.themov.viewmodels.MainViewModel
import com.exam.themov.viewmodels.SortViewModel
import com.exam.themov.viewmodels.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch

class SortAndFilterActivity : AppCompatActivity() ,AdapterView.OnItemSelectedListener{
    private lateinit var binding : ActivitySortAndFilterBinding
    private val viewModel:SortViewModel by viewModels()
    lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySortAndFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val request = RetrofitHelper.getInstance().create(Request::class.java)
        val popularRepository = AnimeRepository(request)
        mainViewModel = ViewModelProvider(this, ViewModelFactory(popularRepository)).get(MainViewModel::class.java)


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


        //For Default
        if(viewPager.currentItem ==0){
            getTotalResultAndGenre(10765)
        }
        tabLayout.setOnTabSelectedListener(object :
            TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> getTotalResultAndGenre(10765)
                    1 -> getTotalResultAndGenre(35)
                    2 -> getTotalResultAndGenre(10759)
                    3 -> getTotalResultAndGenre(10749)
                    4 -> getTotalResultAndGenre(9648)
                    5 -> getTotalResultAndGenre(37)

                }
                super.onTabSelected(tab)
            }
        })


        var spinnerAdapter= ArrayAdapter.createFromResource(this,R.array.Sort,R.layout.selected_item)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
        binding.spinnerSort.adapter=spinnerAdapter
        binding.spinnerSort.onItemSelectedListener=this

        var g=binding.spinnerSort.selectedItem.toString()

        binding.ivBackButton.setOnClickListener {
            onBackPressed()
        }

    }
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val text:String=p0?.getItemAtPosition(p2).toString()
        viewModel.setSort(text)
        //intent.putExtra("selectedSpinner",text)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
    private fun getTotalResultAndGenre(genreId : Int){
        var totalResult = 0
        mainViewModel.viewModelScope.launch {
            var result = mainViewModel.getAnimeByGenre(genreId.toString())
            totalResult = result.body()!!.total_results
            binding.tvTotalResult.text = "Total Result : $totalResult"
            Log.d("ggg", "getTotalResult: $totalResult ")
        }



    }

}
