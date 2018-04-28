package com.publicmethod.androtron.datatron.reducers

import com.publicmethod.androtron.datatron.results.ActionResult
import com.publicmethod.androtron.datatron.states.State
import kotlinx.coroutines.experimental.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.experimental.channels.SendChannel

interface Reducer<in R : ActionResult, out S : State> {

    data class Message<out R : ActionResult, in S : State>(
            val result: R,
            private val receiver: ConflatedBroadcastChannel<S>
    ) : SendChannel<S> by receiver

    fun reduce(message: Message<R, S>)

}