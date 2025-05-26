package com.boxbox.simon.model

data class SimonState (
    val sequence: List<SimonMove> = emptyList(),
    val userIndex: Int = 0,
    val score: Int = 0,
    val state: GamePhase = GamePhase.Idle,
    val difficulty: Difficulty = Difficulty.EASY
)

enum class GamePhase{
    Idle, ShowingSequence, WaitingInput, GameOver
}
enum class SimonMove{
    RED, GREEN, BLUE, YELLOW
}

enum class Difficulty (val diffName: String, val sequenceSpeed: Int, val timeDuration: Int){
    EASY("easy", 600, 3000),
    MEDIUM("medium", 400, 2000),
    HARD("hard", 200, 1500),
    EXTREME("extreme", 100, 1000)
}