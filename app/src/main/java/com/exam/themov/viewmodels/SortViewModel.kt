package com.exam.themov.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SortViewModel : ViewModel() {

    val sortValue : MutableLiveData<String> =  MutableLiveData()

    fun setSort(name:String){
        sortValue.postValue(name)

    }
}