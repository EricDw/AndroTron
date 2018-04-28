package com.publicmethod.androtron.datatron.navigators

import com.publicmethod.androtron.datatron.navigations.Navigation

interface Navigator<in N : Navigation> {
    fun navigate(navigation: N)
}