package com.publicmethod.androtron.datatron.reducers.prime

import com.publicmethod.androtron.datatron.coroutines.ContextProvider
import com.publicmethod.androtron.datatron.navigations.Navigation
import com.publicmethod.androtron.datatron.reducers.NavigationReducer
import com.publicmethod.androtron.datatron.results.NavigationActionResult
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.launch

abstract class PrimeNavigationReducer<in R : NavigationActionResult, out N : Navigation>(
        protected val contextProvider: ContextProvider)
    : NavigationReducer<R, N> {

    private val navigationActor = actor<NavigationReducer.Message<R, N>>(
            contextProvider.IO,
            Channel.UNLIMITED) {
        consumeEach {
            it.send(navigationFromResult(it.result))
        }
    }

    override fun reduce(message: NavigationReducer.Message<R, N>) {
        launch(contextProvider.IO) {
            navigationActor.send(message)
        }
    }

    protected abstract fun navigationFromResult(result: R): N
}