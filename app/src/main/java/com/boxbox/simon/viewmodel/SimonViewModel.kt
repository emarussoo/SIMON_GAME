package com.boxbox.simon.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SimonViewModel : ViewModel(){
    private val _counter = MutableStateFlow(0)
    val counter: StateFlow<Int> = _counter

    fun increment(){
        _counter.value += 1
    }

    fun onColorClicked(){
        increment()
    }
}