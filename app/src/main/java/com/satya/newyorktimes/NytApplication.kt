package com.satya.newyorktimes

import android.app.Application
import com.satya.newyorktimes.injection.appModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.androidModule

class NytApplication: Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidModule(this@NytApplication))
        import(appModule)
    }

    override fun onCreate() {
        super.onCreate()
        kodeinInstance = kodein
    }

    companion object {
        // static instance of kodein to be made available everywhere easily
        lateinit var kodeinInstance: Kodein
    }
}