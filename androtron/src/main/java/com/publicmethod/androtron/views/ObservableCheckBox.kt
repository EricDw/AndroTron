package com.publicmethod.androtron.views

import android.content.Context
import android.util.AttributeSet
import android.widget.CheckBox
import android.widget.CompoundButton
import com.publicmethod.androtron.livedata.EventLiveData

open class ObservableCheckBox(context: Context,
                         attributeSet: AttributeSet? = null)
    : CheckBox(
        context,
        attributeSet) {

    val checkedEventStream = EventLiveData<Pair<CompoundButton, Boolean>>()

    init {
        setOnCheckedChangeListener { compoundButton, b ->
            checkedEventStream.onEvent(Pair(compoundButton, b))
        }
    }

}