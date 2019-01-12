package com.satya.newyorktimes.injection

import com.satya.newyorktimes.data.util.AppExecutors
import com.satya.newyorktimes.data.api.ApiClient
import com.satya.newyorktimes.data.api.ApiInterface
import com.satya.newyorktimes.data.db.DbProvider
import com.satya.newyorktimes.data.db.NewsDB
import com.satya.newyorktimes.data.db.NewsDao
import com.satya.newyorktimes.data.db.SectionDao
import com.satya.newyorktimes.data.repo.NewsRepository
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

/**
 * DI module that provides objects which will live during the application lifecycle.
 */
val appModule = Kodein.Module("AppModule") {
    bind<AppExecutors>() with provider {
        AppExecutors()
    }
    bind<NewsDB>() with singleton {
        DbProvider.getDb(instance())
    }
    bind<NewsDao>() with singleton {
        DbProvider.getNewsDao(instance())
    }
    bind<SectionDao>() with singleton {
        DbProvider.getSectionDao(instance())
    }

    bind<ApiInterface>() with singleton {
        ApiClient.getRetrofitClient().create(ApiInterface::class.java)
    }

    bind<NewsRepository>() with singleton {
        NewsRepository(instance(),instance(),instance(),instance())
    }
}