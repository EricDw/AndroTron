package com.publicmethod.androtron.datatron.circuits.implementation

import com.publicmethod.androtron.datatron.actions.Action
import com.publicmethod.androtron.datatron.circuits.base.Circuits
import com.publicmethod.androtron.datatron.commands.Command
import com.publicmethod.androtron.datatron.coroutines.ContextProvider
import com.publicmethod.androtron.datatron.interpreters.Interpreter
import com.publicmethod.androtron.datatron.processors.Processor
import com.publicmethod.androtron.datatron.reducers.Reducer
import com.publicmethod.androtron.datatron.reducers.Reducer.Message
import com.publicmethod.androtron.datatron.renderers.Renderer
import com.publicmethod.androtron.datatron.results.ActionResult
import com.publicmethod.androtron.datatron.states.State
import kotlinx.coroutines.experimental.channels.*
import kotlinx.coroutines.experimental.launch

abstract class BrainCircuits<
        C : Command,
        A : Action,
        R : ActionResult,
        S : State>(

        val commandStream: ConflatedBroadcastChannel<C> = uIIntentStream(),
        val actionStream: ConflatedBroadcastChannel<A> = actionStream(),
        val resultStream: ConflatedBroadcastChannel<R> = actionResultStream(),
        val viewStateStream: ConflatedBroadcastChannel<S> = viewStateStream(),
        private val contextProvider: ContextProvider,
        protected open val interpreter: Interpreter<C, A>,
        protected open val processor: Processor<A, R>,
        protected open val reducer: Reducer<R, S>,
        protected open val renderer: Renderer<S>

) : Circuits,
        Interpreter<C, A> by interpreter,
        Processor<A, R> by processor,
        Reducer<R, S> by reducer,
        Renderer<S> by renderer {

    interface BrainCircuitry : Circuits

    private val intentActor = actor<C>(
            contextProvider.IO,
            Channel.UNLIMITED) {
        consumeEach {
            launch(contextProvider.IO) {
                actionStream.send(interpret(it))
            }
        }
    }

    private val actionActor = actor<A>(
            contextProvider.IO,
            Channel.UNLIMITED) {
        consumeEach {
            launch(contextProvider.IO) {
                resultStream.send(process(it))
            }
        }
    }

    private val resultActor = actor<R>(
            contextProvider.IO,
            Channel.UNLIMITED) {
        consumeEach {
            launch(contextProvider.IO) {
                reduce(Message(it, viewStateStream))
            }
        }
    }

    private val viewStateActor = actor<S>(
            contextProvider.IO,
            Channel.UNLIMITED) {
        consumeEach {
            launch(contextProvider.MAIN) {
                render(it)
            }
        }
    }

    init {
        launch(contextProvider.IO) {

            launch(contextProvider.IO) {
                subscribeIntentStreamToChannels(intentActor)
            }

            launch(contextProvider.IO) {

                subscribeActionStreamToChannels(actionActor)
            }

            launch(contextProvider.IO) {

                subscribeResultStreamToChannels(resultActor)
            }

            launch(contextProvider.IO) {

                subscribeViewStateStreamToChannels(viewStateActor)
            }
        }
    }

    private suspend fun subscribeIntentStreamToChannels(vararg channels: SendChannel<C>) {
        channels.forEach {
            commandStream.subscribeToChannel(it)
        }
    }

    private suspend fun subscribeActionStreamToChannels(vararg channels: SendChannel<A>) {
        channels.forEach {
            actionStream.subscribeToChannel(it)
        }
    }

    private suspend fun subscribeResultStreamToChannels(vararg channels: SendChannel<R>) {
        channels.forEach {
            resultStream.subscribeToChannel(it)
        }
    }

    private suspend fun subscribeViewStateStreamToChannels(vararg channels: SendChannel<S>) {
        channels.forEach {
            viewStateStream.subscribeToChannel(it)
        }
    }

    // Companion Object
    companion object {
        val TAG: String = BrainCircuits::class.java.simpleName
    }

}

fun <C : Command> uIIntentStream() = ConflatedBroadcastChannel<C>()
fun <A : Action> actionStream() = ConflatedBroadcastChannel<A>()
fun <R : ActionResult> actionResultStream() = ConflatedBroadcastChannel<R>()
fun <S : State> viewStateStream() = ConflatedBroadcastChannel<S>()

suspend fun <E> ConflatedBroadcastChannel<E>.subscribeToChannel(sendChannel: SendChannel<E>) {
    openSubscription().consumeEach { sendChannel.send(it) }
}