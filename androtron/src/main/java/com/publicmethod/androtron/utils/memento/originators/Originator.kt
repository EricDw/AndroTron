package com.publicmethod.androtron.utils.memento.originators

import com.publicmethod.androtron.utils.memento.Memento

open class Originator<T>(protected var value: T) {

    fun setNewValue(newValue: T) {
        value = newValue
    }

    fun restoreFromMemento(memento: Memento<T>): T {
        return memento.getSavedValue()
    }

    fun storeInMemento(): Memento<T> {
        return Memento(value)
    }
}