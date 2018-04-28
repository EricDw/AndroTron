package com.publicmethod.androtron.datatron.circuits.implementation

import com.publicmethod.androtron.datatron.actions.NavigationAction
import com.publicmethod.androtron.datatron.circuits.base.Circuits
import com.publicmethod.androtron.datatron.commands.NavigationCommand
import com.publicmethod.androtron.datatron.coroutines.ContextProvider
import com.publicmethod.androtron.datatron.interpreters.NavigationInterpreter
import com.publicmethod.androtron.datatron.navigations.Navigation
import com.publicmethod.androtron.datatron.navigators.Navigator
import com.publicmethod.androtron.datatron.processors.NavigationProcessor
import com.publicmethod.androtron.datatron.reducers.NavigationReducer
import com.publicmethod.androtron.datatron.reducers.NavigationReducer.Message
import com.publicmethod.androtron.datatron.results.NavigationActionResult
import kotlinx.coroutines.experimental.channels.*
import kotlinx.coroutines.experimental.launch

abstract class NavigationCircuits<
        C : NavigationCommand,
        A : NavigationAction,
        R : NavigationActionResult,
        N : Navigation>(

        val navigationCommandStream: ConflatedBroadcastChannel<C> = navigationCommandStream(),
        val navigationActionStream: ConflatedBroadcastChannel<A> = navigationActionStream(),
        val navigationResultStream: ConflatedBroadcastChannel<R> = navigationActionResultStream(),
        val navigationStream: ConflatedBroadcastChannel<N> = navigationStream(),
        private val contextProvider: ContextProvider,
        protected open val interpreter: NavigationInterpreter<C, A>,
        protected open val processor: NavigationProcessor<A, R>,
        protected open val reducer: NavigationReducer<R, N>,
        protected open val navigator: Navigator<N>)

    : Circuits,
        NavigationInterpreter<C, A> by interpreter,
        NavigationProcessor<A, R> by processor,
        NavigationReducer<R, N> by reducer,
        Navigator<N> by navigator,
        SendChannel<C> by navigationCommandStream {

    interface NavigationCircuits : Circuits

    private val navigationCommandActor = actor<C>(
            contextProvider.IO,
            Channel.UNLIMITED) {
        consumeEach {
            launch(contextProvider.IO) {
                navigationActionStream.send(interpret(it))
            }
        }
    }

    private val navigationActionActor = actor<A>(
            contextProvider.IO,
            Channel.UNLIMITED) {
        consumeEach {
            launch(contextProvider.IO) {
                navigationResultStream
                        .send(process(it))
            }
        }
    }

    private val navigationActionResultActor = actor<R>(
            contextProvider.IO,
            Channel.UNLIMITED) {
        consumeEach {
            launch(contextProvider.IO) {
                reduce(Message(it, navigationStream))
            }
        }
    }

    private val navigationActor = actor<N>(
            contextProvider.IO,
            Channel.UNLIMITED) {
        consumeEach {
            launch(contextProvider.MAIN) {
                navigate(it)
            }
        }
    }

    init {
        launch(contextProvider.IO) {
            launch(contextProvider.IO) {
                subscribeNavigationIntentStreamToChannels(navigationCommandActor)
            }
            launch(contextProvider.IO) {
                subscribeNavigationActionStreamToChannels(navigationActionActor)
            }
            launch(contextProvider.IO) {
                subscribeNavigationResultStreamToChannels(navigationActionResultActor)
            }
            launch(contextProvider.IO) {
                subscribeNavigationStreamToChannels(navigationActor)
            }
        }
    }

    private suspend fun subscribeNavigationIntentStreamToChannels(vararg channels: SendChannel<C>) {
        channels.forEach {
            navigationCommandStream.subscribeToChannel(it)
        }
    }

    private suspend fun subscribeNavigationActionStreamToChannels(vararg channels: SendChannel<A>) {
        channels.forEach {
            navigationActionStream.subscribeToChannel(it)
        }
    }

    private suspend fun subscribeNavigationResultStreamToChannels(vararg channels: SendChannel<R>) {
        channels.forEach {
            navigationResultStream.subscribeToChannel(it)
        }
    }

    private suspend fun subscribeNavigationStreamToChannels(vararg channels: SendChannel<N>) {
        channels.forEach {
            navigationStream.subscribeToChannel(it)
        }
    }
}

fun <C : NavigationCommand> navigationCommandStream() = ConflatedBroadcastChannel<C>()
fun <A : NavigationAction> navigationActionStream() = ConflatedBroadcastChannel<A>()
fun <R : NavigationActionResult> navigationActionResultStream() = ConflatedBroadcastChannel<R>()
fun <N : Navigation> navigationStream() = ConflatedBroadcastChannel<N>()
