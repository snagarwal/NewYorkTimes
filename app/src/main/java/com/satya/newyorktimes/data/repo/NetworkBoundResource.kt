package com.satya.newyorktimes.data.repo


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import android.util.Log
import com.satya.newyorktimes.data.api.ApiEmptyResponse
import com.satya.newyorktimes.data.api.ApiErrorResponse
import com.satya.newyorktimes.data.api.ApiResponse
import com.satya.newyorktimes.data.api.ApiSuccessResponse
import com.satya.newyorktimes.data.util.AppExecutors
import com.satya.newyorktimes.data.model.Error
import com.satya.newyorktimes.data.model.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 * @param <ResultType>
 * @param <RequestType>
</RequestType></ResultType> */
abstract class NetworkBoundResource<ResultType, RequestType>
@MainThread constructor(private val appExecutors: AppExecutors) {

    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)
        @Suppress("LeakingThis")
        val dbSource = loadFromDb()
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData ->
                    setValue(Resource.success(newData))
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createCall()
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource) { newData ->
            setValue(Resource.loading(newData))
        }
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)
            when (response) {
                is ApiSuccessResponse -> {
                    appExecutors.diskIO().execute {
                        saveCallResult(processResponse(response))
                        appExecutors.mainThread().execute {
                            // we specially request a new live data,
                            // otherwise we will get immediately last cached value,
                            // which may not be updated with latest results received from network.
                            result.addSource(loadFromDb()) { newData ->
                                setValue(Resource.success(newData))
                            }
                        }
                    }
                }
                is ApiEmptyResponse -> {
                    appExecutors.mainThread().execute {
                        // reload from disk whatever we had
                        result.addSource(loadFromDb()) { newData ->
                            setValue(Resource.success(newData))
                        }
                    }
                }
                is ApiErrorResponse -> {
                    onFetchFailed()
                    result.addSource(dbSource) { newData ->
                        setValue(Resource.error(Error.build(response.errorMessage), newData))
                    }
                }
            }
        }

    }

    private fun processResponse(response: ApiSuccessResponse<RequestType>): RequestType =
        response.body

    protected open fun onFetchFailed() {}

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>
}