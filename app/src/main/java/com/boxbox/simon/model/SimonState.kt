package com.boxbox.simon.model

data class SimonState (
    val sequence: List<SimonMove> = emptyList(),
    val userIndex: Int = 0,
    val score: Int = 0,
    val state: GamePhase = GamePhase.Idle
)

enum class GamePhase{
    Idle, ShowingSequence, WaitingInput, GameOver
}
enum class SimonMove{
    RED, GREEN, BLUE, YELLOW
}