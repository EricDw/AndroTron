package com.publicmethod.androtron.chambers

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import com.publicmethod.androtron.datatron.actions.Action
import com.publicmethod.androtron.datatron.actions.NavigationAction
import com.publicmethod.androtron.datatron.circuits.implementation.BrainCircuits
import com.publicmethod.androtron.datatron.circuits.implementation.NavigationCircuits
import com.publicmethod.androtron.datatron.commands.Command
import com.publicmethod.androtron.datatron.commands.NavigationCommand
import com.publicmethod.androtron.datatron.coroutines.ContextProvider
import com.publicmethod.androtron.datatron.navigations.Navigation
import com.publicmethod.androtron.datatron.navigators.Navigator
import com.publicmethod.androtron.datatron.renderers.Renderer
import com.publicmethod.androtron.datatron.results.ActionResult
import com.publicmethod.androtron.datatron.results.NavigationActionResult
import com.publicmethod.androtron.datatron.states.State
import com.publicmethod.androtron.livedata.NavigationLiveData
import kotlinx.coroutines.experimental.launch

abstract class PrimeCranialChamber<
        // Generics
        C : Command,
        A : Action,
        R : ActionResult,
        S : State,
        NC : NavigationCommand,
        NA : NavigationAction,
        NAR : NavigationActionResult,
        N : Navigation>(
        // Constructor
        private val contextProvider: ContextProvider)
//  Abstract Class
    : ViewModel(),
        // Interfaces
        CranialChamber<C, S, NC, N>,
        Renderer<S>,
        Navigator<N> {

    //region Override Properties
    override val viewStateLiveData: LiveData<S> = MediatorLiveData<S>()
    override val navigationStreamLiveData: LiveData<N> = NavigationLiveData<N>()
    //endregion

    //region Private Properties
    private val commandStreamLiveData = MediatorLiveData<C>()
    private val navigationCommandStreamLiveData = MediatorLiveData<NC>()
    //endregion

    //region Protected Properties
    protected abstract val brainCircuits: BrainCircuits<C, A, R, S>
    protected abstract val navigationCircuits: NavigationCircuits<NC, NA, NAR, N>
    //endregion

    //region Override Functions
    override fun issueCommand(command: C) {
        commandStreamLiveData.value = command
    }

    override fun issueNavigationCommand(navigationCommand: NC) {
        navigationCommandStreamLiveData.value = navigationCommand
    }
    //endregion

    //region Protected Functions
    protected fun subscribeIntentStreamToIntentStreamLiveData() {
        commandStreamLiveData.observeForever {
            it?.run {
                launch(contextProvider.IO) { brainCircuits.commandStream.send(it) }
            }
        }
    }

    protected fun subscribeNavigationStreamToNavigationStreamLiveData() {
        navigationCommandStreamLiveData.observeForever {
            it?.run {
                launch(contextProvider.IO) {
                    navigationCircuits.send(it)
                }
            }
        }
    }
    //endregion
}

