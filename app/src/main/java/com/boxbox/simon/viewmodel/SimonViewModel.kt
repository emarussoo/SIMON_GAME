package com.boxbox.simon.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.boxbox.simon.model.GamePhase
import com.boxbox.simon.model.SimonMove
import com.boxbox.simon.model.SimonState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class SimonViewModel : ViewModel(){
    private val _highlightedMove = MutableStateFlow<SimonMove?>(null)
    val highlightedMove: StateFlow<SimonMove?> = _highlightedMove.asStateFlow()

    ////////////////////////////
    private val _gameState = MutableStateFlow(SimonState())
    val gameState: StateFlow<SimonState> = _gameState.asStateFlow()

    ///////////////////////////////
    private val _timerKey = MutableStateFlow(0)
    val timerKey: StateFlow<Int> = _timerKey

    fun resetTimer() {
        _timerKey.value++ // Cambia chiave per resettare
    }

    fun StartGame(){
        val firstMove = SimonMove.values().random()
        _gameState.value = SimonState(
            sequence = listOf(firstMove),
            score = 0,
            state = GamePhase.ShowingSequence
        )

        showSequence()
    }

    fun EndGame(){
        _gameState.value = SimonState(
            state = GamePhase.GameOver
        )
    }

    fun showSequence(){
        viewModelScope.launch {
            _gameState.value = _gameState.value.copy(state = GamePhase.ShowingSequence)
            delay(900L)
            for((index, move) in _gameState.value.sequence.withIndex()){
                _highlightedMove.value = move
                delay(400L)
                _highlightedMove.value = null
                delay(200L)
            }

            onSequenceShown()
        }
    }

    fun onUserInput(move: SimonMove){
        val current = _gameState.value
        if(current.state != GamePhase.WaitingInput) return

        val expctedMove = current.sequence[current.userIndex]
        if(move == expctedMove){
            resetTimer()
            val nextIndex = current.userIndex + 1
            if(nextIndex == current.sequence.size){
                val newMove = SimonMove.values().random()
                _gameState.value = current.copy(
                    sequence = current.sequence + newMove,
                    userIndex = 0,
                    score = current.score + 1,
                    state = GamePhase.ShowingSequence
                )
                showSequence()
            }else{
                _gameState.value = current.copy(userIndex = nextIndex)
            }
        }else{
            _gameState.value = current.copy(state = GamePhase.GameOver)
        }
    }


    fun onSequenceShown(){
        _gameState.value = _gameState.value.copy(state = GamePhase.WaitingInput)
    }
}