package com.boxbox.simon.viewmodel

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boxbox.simon.model.DB.DBAccess
import com.boxbox.simon.model.GamePhase
import com.boxbox.simon.model.DB.ScoreEntity
import com.boxbox.simon.model.Difficulty
import com.boxbox.simon.model.SimonMove
import com.boxbox.simon.model.SimonState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import java.util.Date
import java.util.Locale

class SimonViewModel() : ViewModel(){

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
            state = GamePhase.ShowingSequence,
            difficulty = this.gameState.value.difficulty
        )

        showSequence()
    }

    fun EndGame(context: Context): SimonState {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDateTime = formatter.format(Date())


        val newScore = ScoreEntity(score = gameState.value.score ,gameDate = currentDateTime, difficulty = gameState.value.difficulty.diffName.toString())

        viewModelScope.launch {
            val db = DBAccess.getDB(context)
            db.scoreDAO().insertScore(newScore)

            val savedScores = db.scoreDAO().getTop10Scores()
            savedScores.forEach{
                Log.d("ScoreCheck", "Punteggio: ${it.score} | Data: ${it.gameDate} | Diff: ")
           }
        }


        _gameState.value = SimonState(
            state = GamePhase.GameOver,
            difficulty = this.gameState.value.difficulty
        )
        return gameState.value
    }

    fun showSequence(){
        viewModelScope.launch {
            _gameState.value = _gameState.value.copy(state = GamePhase.ShowingSequence)
            delay(900L)
            for((index, move) in _gameState.value.sequence.withIndex()){
                if(_gameState.value.state == GamePhase.GameOver){
                    break
                }
                _highlightedMove.value = move

                ////////////////
                delay(gameState.value.difficulty.sequenceSpeed.toLong())  //difficoltà
                ////////////////

                _highlightedMove.value = null
                delay(200L)
            }

            if(_gameState.value.state != GamePhase.GameOver){
                onSequenceShown()
            }
        }
    }

    fun onUserInput(move: SimonMove, context: Context){
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
            EndGame(context)
        }
    }


    fun onSequenceShown(){
        _gameState.value = _gameState.value.copy(state = GamePhase.WaitingInput)
    }

    fun setIdle(){
        _gameState.value = _gameState.value.copy(state = GamePhase.Idle)
    }

    fun setDifficulty(diff: Difficulty){
        setIdle()
        _gameState.value = _gameState.value.copy(difficulty = diff)

    }
}