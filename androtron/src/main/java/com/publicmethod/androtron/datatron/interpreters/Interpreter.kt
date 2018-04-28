package com.publicmethod.androtron.datatron.interpreters

import com.publicmethod.androtron.datatron.actions.Action
import com.publicmethod.androtron.datatron.commands.Command

interface Interpreter<in C : Command, out A : Action> {

    fun interpret(command: C): A

}
