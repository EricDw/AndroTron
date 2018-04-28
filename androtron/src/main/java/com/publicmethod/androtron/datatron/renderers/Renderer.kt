package com.publicmethod.androtron.datatron.renderers

import com.publicmethod.androtron.datatron.states.State

interface Renderer<in S : State> {
    fun render(viewState: S)
}