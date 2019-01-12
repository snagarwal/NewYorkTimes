package com.satya.newyorktimes.data.util;

import android.arch.lifecycle.LiveData;

/**
 * A LiveData class that has {@code null} value.
 */
public final class AbsentLiveData extends LiveData {
    private AbsentLiveData() {
        postValue(null);
    }

    public static <T> LiveData<T> create() {
        //noinspection unchecked
        return new AbsentLiveData();
    }
}
