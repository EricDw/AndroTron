package com.publicmethod.androtron.contexts

import com.publicmethod.androtron.datatron.coroutines.ContextProvider
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlin.coroutines.experimental.CoroutineContext

open class CoroutineContextProvider : ContextProvider {
    override val MAIN: CoroutineContext by lazy { UI }
    override val IO: CoroutineContext by lazy { CommonPool }
}