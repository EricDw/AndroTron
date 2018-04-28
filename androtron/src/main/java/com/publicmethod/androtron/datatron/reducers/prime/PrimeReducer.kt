package com.publicmethod.androtron.datatron.reducers.prime

import com.publicmethod.androtron.datatron.coroutines.ContextProvider
import com.publicmethod.androtron.datatron.reducers.Reducer
import com.publicmethod.androtron.datatron.results.ActionResult
import com.publicmethod.androtron.datatron.states.State
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.launch

abstract class PrimeReducer<in R: ActionResult, S: State>(
        private val initialState: S,
        protected val contextProvider: ContextProvider)
    : Reducer<R, S> {

    private val reducerActor = actor<Reducer.Message<R, S>>(
            contextProvider.IO,
            Channel.UNLIMITED) {
        var viewState = initialState
        consumeEach {
            viewState = viewStateFromResult(it.result, viewState)
            it.send(viewState)
        }
    }

    override fun reduce(message: Reducer.Message<R, S>) {
        launch(contextProvider.IO) {
            reducerActor.send(message)
        }
    }

    protected abstract fun viewStateFromResult(result: R, oldViewState: S): S
}