package com.satya.newyorktimes.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.satya.newyorktimes.NytApplication
import com.satya.newyorktimes.data.model.News
import com.satya.newyorktimes.data.model.Resource
import com.satya.newyorktimes.data.model.Section
import com.satya.newyorktimes.data.repo.NewsRepository
import com.satya.newyorktimes.data.util.AbsentLiveData
import org.kodein.di.generic.instance

class NewsViewModel : ViewModel() {
    private val newsRepository: NewsRepository by NytApplication.kodeinInstance.instance()

    var newsFeed: LiveData<Resource<List<News>>>
    private val newsFeedMutableLiveData = MutableLiveData<String>()

    var sections: LiveData<Resource<List<Section>>>
    private val sectionMutableLiveData = MutableLiveData<Boolean>()

    var news: LiveData<Resource<News>>
    private val newsMutableLiveData = MutableLiveData<String>()

    init {
        newsFeed = Transformations.switchMap(newsFeedMutableLiveData) {
            if (it.isNullOrEmpty() || it == "All") {
                newsRepository.fetchNewsFeeds()
            } else {
                newsRepository.getNewsBySection(it)
            }
        }

        news = Transformations.switchMap(newsMutableLiveData) {
            if (it != null) {
                newsRepository.getNewsByTitle(it)
            } else {
                AbsentLiveData.create<Resource<News>>()
            }
        }

        sections = Transformations.switchMap(sectionMutableLiveData) {
            if (it == true) {
                newsRepository.getAllSection()
            } else {
                AbsentLiveData.create<Resource<List<Section>>>()
            }
        }
    }

    fun getNewsFeed(section: String? = null) {
        newsFeedMutableLiveData.value = section
    }

    fun getNewsByTitle(title: String) {
        newsMutableLiveData.value = title

    }

    fun getAllSections() {
        sectionMutableLiveData.value = true

    }

}