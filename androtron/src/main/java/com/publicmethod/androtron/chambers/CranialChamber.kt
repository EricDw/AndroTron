package com.publicmethod.androtron.chambers

import android.arch.lifecycle.LiveData
import com.publicmethod.androtron.datatron.commands.Command
import com.publicmethod.androtron.datatron.commands.NavigationCommand
import com.publicmethod.androtron.datatron.navigations.Navigation
import com.publicmethod.androtron.datatron.states.State

interface CranialChamber<
        in C : Command,
        S : State,
        in NC : NavigationCommand,
        N : Navigation> {

    fun issueCommand(command: C)
    fun issueNavigationCommand(navigationCommand: NC)
    val viewStateLiveData: LiveData<S>
    val navigationStreamLiveData: LiveData<N>

}