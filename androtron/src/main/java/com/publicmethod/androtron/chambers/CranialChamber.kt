package com.publicmethod.androtron.chambers

import android.arch.lifecycle.LiveData
import com.publicmethod.androtron.datatron.commands.Command
import com.publicmethod.androtron.datatron.commands.NavigationCommand
import com.publicmethod.androtron.datatron.navigations.Navigation
import com.publicmethod.androtron.datatron.states.State

/**
 * Defines the interface for interacting with
 * AndroTrons [PrimeCranialChamber]
 */
interface CranialChamber<
        in C : Command,
        S : State,
        in NC : NavigationCommand,
        N : Navigation> {

    fun issueCommand(command: C)
    fun issueNavigationCommand(navigationCommand: NC)
    val viewStateLiveDataStream: LiveData<S>
    val navigationLiveDataStream: LiveData<N>

}