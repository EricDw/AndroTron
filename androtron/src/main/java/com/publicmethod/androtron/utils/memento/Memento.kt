package com.publicmethod.androtron.utils.memento

open class Memento<out T>(private var value: T) {
    fun getSavedValue(): T {
        return value
    }
}