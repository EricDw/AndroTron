package com.publicmethod.androtron.datatron.coroutines

import kotlin.coroutines.experimental.CoroutineContext

interface ContextProvider {
    val MAIN: CoroutineContext
    val IO: CoroutineContext
}