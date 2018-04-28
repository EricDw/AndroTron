package com.publicmethod.androtron.utils.undoable

import com.publicmethod.androtron.utils.memento.caretakers.Caretaker

abstract class Undoable<in T>(value: T) {

    private val caretaker = Caretaker(value)

    init {
        addNewMemento(value)
    }

    fun addNewMemento(value: T) {
        caretaker.run {
            setNewValue(value)
            addMemento(storeInMemento())
        }
    }

    fun canUndo() = caretaker.hasNext()

    fun undo() = onUndo(caretaker.getNext())


    protected abstract fun onUndo(value: T)

    fun canRedo() = caretaker.hasPrevious()

    fun redo() = onRedo(caretaker.getPrevious())

    protected abstract fun onRedo(value: T)

}