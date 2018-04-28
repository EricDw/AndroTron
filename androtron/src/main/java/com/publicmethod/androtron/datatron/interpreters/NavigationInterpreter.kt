package com.publicmethod.androtron.datatron.interpreters

import com.publicmethod.androtron.datatron.actions.NavigationAction
import com.publicmethod.androtron.datatron.commands.NavigationCommand

interface NavigationInterpreter<in C : NavigationCommand, out A : NavigationAction> {
    fun interpret(command: C): A
}