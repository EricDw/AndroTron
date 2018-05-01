package com.publicmethod.androtron.livedata

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.support.annotation.MainThread

/**
 *  Changes to EventLiveData can only be observed once.
 *
 *  EventLiveData does *NOT* store it's value.
 *
 *  Therefore [EventLiveData.getValue] will always return null.
 *
 *  This behavior makes it good for one off events like button clicks
 *
 *  and other such events that need to be observed.
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

    /**
     * Changes the value of this EventLiveData
     *
     * Changes to the value will not be maintained
     * therefore [EventLiveData.getValue] will always return null.
     *
     * @param event The event that needs to be observed i.e. a button click
     */
    @MainThread
    fun onEvent(event: T) {
        value = event
    }
}