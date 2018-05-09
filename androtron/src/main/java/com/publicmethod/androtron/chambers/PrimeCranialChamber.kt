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

    /**
     * A [LiveData] object containing the [State]
     * of the View.
     */
    override val viewStateLiveDataStream: LiveData<S> = MediatorLiveData<S>()

    /**
     * A [LiveData] object containing the [Navigation]s
     * that need to be performed.
     * Internally [NavigationLiveData] is used so the [Navigation]
     * will not be saved therefore preventing odd behavior when resubscribing.
     */
    override val navigationLiveDataStream: LiveData<N> = NavigationLiveData()
    //endregion Override Properties

    //region Private Properties
    private val commandStreamLiveData = MediatorLiveData<C>()
    private val navigationCommandStreamLiveData = MediatorLiveData<NC>()
    //endregion

    //region Protected Properties
    /**
     * @see BrainCircuits
     */
    protected abstract val brainCircuits: BrainCircuits<C, A, R, S>

    /**
     * @see NavigationCircuits
     */
    protected abstract val navigationCircuits: NavigationCircuits<NC, NA, NAR, N>
    //endregion

    //region Override Functions

    /**
     * Sets the [commandStreamLiveData] value to [command]
     *
     * @param command The [Command] to be processed
     */
    override fun issueCommand(command: C) {
        commandStreamLiveData.value = command
    }

    /**
     * Sets the [navigationCommandStreamLiveData] value to [navigationCommand]
     *
     * @param navigationCommand The [NavigationCommand] to be processed
     */
    override fun issueNavigationCommand(navigationCommand: NC) {
        navigationCommandStreamLiveData.value = navigationCommand
    }
    //endregion

    //region Protected Functions

    /**
     * Subscribes [brainCircuits] to the [commandStreamLiveData]
     * so the [Command]s can be processed.
     *
     * This function should be called at some point before
     * the view subscribes to it.
     *
     * The reason this function is not called automatically
     * is to provide flexibility to users using injection libraries
     * such as Dagger or Koin.
     */
    protected fun subscribeIntentStreamToIntentStreamLiveData() {
        commandStreamLiveData.observeForever {
            it?.run {
                launch(contextProvider.IO) { brainCircuits.commandStream.send(it) }
            }
        }
    }

    /**
     * Subscribes [navigationCircuits] to the [navigationCommandStreamLiveData]
     * so the [NavigationCommand]s can be processed.
     *
     * This function should be called at some point before
     * the view subscribes to it.
     *
     * The reason this function is not called automatically
     * is to provide flexibility to users using injection libraries
     * such as Dagger or Koin.
     */
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

