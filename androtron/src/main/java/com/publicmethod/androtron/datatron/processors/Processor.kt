package com.publicmethod.androtron.datatron.processors

import com.publicmethod.androtron.datatron.actions.Action
import com.publicmethod.androtron.datatron.results.ActionResult

interface Processor<in A : Action, out R : ActionResult> {
    fun process(action: A): R
}