package com.satya.newyorktimes.data.util


import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Global executor pools for the whole application.
 *
 *
 * We provide 3 threads to the NetworkIO Executor.
 * Grouping tasks like this avoids the effects of task starvation (e.g. disk reads don't wait behind
 * service requests).
 */
open class AppExecutors(
    private val diskIO: Executor, private val networkIO: Executor,
    private val mainThread: Executor, private val usageMonitorThread: Executor
) {

    constructor() : this(
        Executors.newSingleThreadExecutor(),
        Executors.newFixedThreadPool(3),
        MainThreadExecutor(), Executors.newSingleThreadExecutor()
    )

    fun diskIO(): Executor {
        return diskIO
    }

    fun networkIO(): Executor {
        return networkIO
    }

    fun mainThread(): Executor {
        return mainThread
    }

    fun usageMonitoringThread(): Executor {
        return usageMonitorThread
    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}
