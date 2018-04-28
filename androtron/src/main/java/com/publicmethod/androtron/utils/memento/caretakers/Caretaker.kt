package com.publicmethod.androtron.utils.memento.caretakers

import com.publicmethod.androtron.utils.memento.Memento
import com.publicmethod.androtron.utils.memento.originators.Originator

class Caretaker<T>(value: T) : Originator<T>(value) {

    private val savedMementos = mutableListOf<Memento<T>>()

    private var currentIndex = -1

    fun addMemento(memento: Memento<T>) {
        savedMementos.add(memento)
        currentIndex++
    }

    fun getNext(): T {
        return when {
            hasNext() -> {
                currentIndex--
                restoreFromMemento(savedMementos[currentIndex])
            }
            else -> restoreFromMemento(savedMementos[0])
        }
    }

    fun hasNext(): Boolean {
        return currentIndex > 0
    }

    fun getPrevious(): T {
        return when {
            hasPrevious() -> {
                currentIndex++
                restoreFromMemento(savedMementos[currentIndex])
            }
            else -> restoreFromMemento(savedMementos[currentIndex])
        }
    }

    fun hasPrevious(): Boolean {
        return currentIndex < savedMementos.size.dec()
    }

}