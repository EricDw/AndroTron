package com.publicmethod.androtron.livedata

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.support.annotation.MainThread

/**
 *
 */
open class EventLiveData<T> : LiveData<T>() {

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
        super.observe(owner, Observer {
            if ( it == null) return@Observer
            observer.onChanged(it)
            value = null
        })
    }

    @MainThread
    override fun observeForever(observer: Observer<T>) {
        super.observeForever(Observer {
            if (it == null) return@Observer
            observer.onChanged(it)
            value = null
        })
    }

    @MainThread
    fun onEvent(data: T) {
        value = data
    }
}