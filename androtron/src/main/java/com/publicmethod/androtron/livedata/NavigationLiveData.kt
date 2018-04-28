package com.publicmethod.androtron.livedata

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.support.annotation.MainThread
import com.publicmethod.androtron.datatron.navigations.Navigation

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

    @MainThread
    fun onNavigation(data: N) {
        value = data
    }
}