package com.publicmethod.androtron.datatron.reducers

import com.publicmethod.androtron.datatron.navigations.Navigation
import com.publicmethod.androtron.datatron.results.NavigationActionResult
import kotlinx.coroutines.experimental.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.experimental.channels.SendChannel

interface NavigationReducer<in R : NavigationActionResult, out N : Navigation> {

    data class Message< out R : NavigationActionResult,  in N : Navigation>(
            val result: R,
            private val receiver: ConflatedBroadcastChannel<N>
    ) : SendChannel<N> by receiver, NavigationActionResult by result

    fun reduce(message: Message<R, N>)

}