package com.publicmethod.androtron.livedata

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.support.annotation.MainThread
import com.publicmethod.androtron.datatron.navigations.Navigation

/**
 *  Changes to NavigationLiveData can only be observed once.
 *
 *  NavigationLiveData does *NOT* store it's value.
 *
 *  Therefore [NavigationLiveData.getValue] will always return null.
 *
 *  This behavior is intended to communicate the applications need to navigate somewhere to
 *  interested parties i.e. Activities and Fragments.
 */
class NavigationLiveData<N : Navigation> : LiveData<N>() {

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<N>) {
        super.observe(owner, Observer {
            if (it == null) return@Observer
            observer.onChanged(it)
            value = null
        })
    }

    @MainThread
    override fun observeForever(observer: Observer<N>) {
        super.observeForever(Observer {
            if (it == null) return@Observer
            observer.onChanged(it)
            value = null
        })
    }

    /**
     * Changes the value of this NavigationLiveData
     *
     * Changes to the value will not be maintained
     * therefore [NavigationLiveData.getValue] will always return null.
     *
     * @param navigation The [Navigation] that needs to be observed
     */
    @MainThread
    fun onNavigation(navigation: N) {
        value = navigation
    }
}