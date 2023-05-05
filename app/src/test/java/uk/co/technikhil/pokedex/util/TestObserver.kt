package uk.co.technikhil.pokedex.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

class TestObserver<T>(private val observed: LiveData<T>) : Observer<T> {

    private val _valueHistory = mutableListOf<T>()
    val valueHistory: List<T>
        get() = _valueHistory

    override fun onChanged(value: T) {
        _valueHistory.add(value)
    }

    fun wasEmitted(value: T) = valueHistory.contains(value)
}