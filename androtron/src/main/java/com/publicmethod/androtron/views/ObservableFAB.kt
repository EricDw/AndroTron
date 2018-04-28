package com.publicmethod.androtron.views

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import com.publicmethod.androtron.livedata.EventLiveData

open class ObservableFAB(context: Context,
                    attributeSet: AttributeSet? = null)
    : FloatingActionButton(
        context,
        attributeSet) {

    val clickEventStream = EventLiveData<FloatingActionButton>()

    init {
        setOnClickListener {
            clickEventStream.onEvent(it as FloatingActionButton)
        }
    }

}