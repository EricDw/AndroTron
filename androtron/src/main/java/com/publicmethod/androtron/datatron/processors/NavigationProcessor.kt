package com.publicmethod.androtron.datatron.processors

import com.publicmethod.androtron.datatron.actions.NavigationAction
import com.publicmethod.androtron.datatron.results.NavigationActionResult

interface NavigationProcessor<in A : NavigationAction, out R : NavigationActionResult> {
    fun process(action: A): R
}