package com.boxbox.simon.model.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScoreDAO {
    @Insert
    suspend fun insertScore(score: ScoreEntity)

    @Query("SELECT * FROM Scores ORDER BY score DESC LIMIT 10")
    suspend fun getTop10Scores(): List<ScoreEntity>

    @Query("DELETE FROM Scores")
    suspend fun deleteAllScores()
}