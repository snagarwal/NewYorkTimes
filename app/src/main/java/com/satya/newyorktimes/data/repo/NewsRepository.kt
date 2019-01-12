package com.satya.newyorktimes.data.repo

import android.arch.lifecycle.LiveData
import com.satya.newyorktimes.data.util.AppExecutors
import com.satya.newyorktimes.data.api.ApiInterface
import com.satya.newyorktimes.data.api.ApiResponse
import com.satya.newyorktimes.data.db.NewsDao
import com.satya.newyorktimes.data.db.SectionDao
import com.satya.newyorktimes.data.model.News
import com.satya.newyorktimes.data.model.Resource
import com.satya.newyorktimes.data.model.NewsFeed
import com.satya.newyorktimes.data.model.Section
import com.satya.newyorktimes.data.util.AbsentLiveData
import com.satya.newyorktimes.data.util.RateLimiter
import com.satya.newyorktimes.util.isNotNullOrEmpty
import retrofit2.Call
import java.util.concurrent.TimeUnit
import kotlin.text.Typography.section

/**
 * Repository class to provide news data to the presentation layer.
 *
 */
class NewsRepository(
    private val appExecutors: AppExecutors,
    private val apiInterface: ApiInterface,
    private val newsDao: NewsDao,
    private val sectionDao: SectionDao
) {
    private val newsFeedRateLimit = RateLimiter<String>(10, TimeUnit.MINUTES)

    fun fetchNewsFeeds(): LiveData<Resource<List<News>>> {
        return object : NetworkBoundResource<List<News>, NewsFeed>(appExecutors) {
            override fun saveCallResult(item: NewsFeed) {
                newsDao.clear()
                newsDao.insert(item.results)
                insertSection(item.results)
            }

            override fun shouldFetch(data: List<News>?): Boolean {
                return data.isNullOrEmpty() || newsFeedRateLimit.shouldFetch("news")
            }

            override fun loadFromDb(): LiveData<List<News>> {
                return newsDao.getAll()
            }

            override fun createCall(): LiveData<ApiResponse<NewsFeed>> {
                return apiInterface.getNewsFeed()
            }
        }.asLiveData()


    }

    private fun insertSection(results: List<News>) {
        if (results.isNotNullOrEmpty()) {
            val sections = arrayListOf<Section>()
            results.forEach {
                it?.section?.let { section ->
                    if (section.isNotEmpty()) {
                        sections.add(Section(section))
                    }
                }
            }
            sectionDao.insert(sections)
        }


    }


    fun getNewsBySection(section: String): LiveData<Resource<List<News>>> {
        return object : NetworkBoundResource<List<News>, NewsFeed>(appExecutors) {
            override fun saveCallResult(item: NewsFeed) {

            }

            override fun shouldFetch(data: List<News>?): Boolean {
                //No network call for news by section.
                return false
            }

            override fun loadFromDb(): LiveData<List<News>> {
                return newsDao.findBySection(section)
            }

            override fun createCall(): LiveData<ApiResponse<NewsFeed>> {
                return apiInterface.getNewsFeed()
            }
        }.asLiveData()
    }


    fun getAllSection(): LiveData<Resource<List<Section>>> {
        return object : NetworkBoundResource<List<Section>, Section>(appExecutors) {
            override fun saveCallResult(item: Section) {

            }

            override fun shouldFetch(data: List<Section>?): Boolean {
                //No network call for news by section.
                return false
            }

            override fun loadFromDb(): LiveData<List<Section>> {
                return sectionDao.getAll()
            }

            override fun createCall(): LiveData<ApiResponse<Section>> {
                return AbsentLiveData.create<ApiResponse<Section>>()
            }
        }.asLiveData()
    }


    fun getNewsByTitle(title: String): LiveData<Resource<News>> {
        return object : NetworkBoundResource<News, News>(appExecutors) {
            override fun saveCallResult(item: News) {
            }

            override fun shouldFetch(data: News?): Boolean {
                //No network call for news by id.
                return false
            }

            override fun loadFromDb(): LiveData<News> {
                return newsDao.findByTitle(title)
            }

            override fun createCall(): LiveData<ApiResponse<News>> {
                return AbsentLiveData.create<ApiResponse<News>>()
            }

        }.asLiveData()
    }
}