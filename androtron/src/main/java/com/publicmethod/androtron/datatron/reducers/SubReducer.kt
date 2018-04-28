package com.publicmethod.androtron.datatron.reducers

import com.publicmethod.androtron.datatron.results.ActionResult
import com.publicmethod.androtron.datatron.states.State

interface SubReducer<in R: ActionResult, S: State> {
    fun viewStateFromResult(result: R, oldViewState: S): S
}