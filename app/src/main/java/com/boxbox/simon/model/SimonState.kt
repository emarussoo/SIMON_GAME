package com.boxbox.simon.model

import com.boxbox.simon.R

data class SimonState (
    val sequence: List<SimonMove> = emptyList(),
    val userIndex: Int = 0,
    val score: Int = 0,
    var state: GamePhase = GamePhase.Idle,
    val difficulty: Difficulty = Difficulty.EASY
)

enum class GamePhase{
    Idle, ShowingSequence, WaitingInput, GameOver
}
enum class SimonMove{
    RED, GREEN, BLUE, YELLOW
}

enum class Difficulty (val index: Int, val diffName: Int, val sequenceSpeed: Int, val timeDuration: Int){
    EASY(0, R.string.easy, 600, 3000),
    MEDIUM(1, R.string.medium, 400, 2000),
    HARD(2, R.string.hard, 200, 1500),
    EXTREME(3, R.string.extreme, 100, 1000)
}