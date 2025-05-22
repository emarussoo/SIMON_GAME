package com.boxbox.simon.model.DB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Scores")
data class ScoreEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val score:Int,
    val gameDate: String
)
