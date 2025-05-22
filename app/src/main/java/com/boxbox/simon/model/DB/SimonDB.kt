package com.boxbox.simon.model.DB

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ScoreEntity::class], version = 1)
abstract class SimonDB : RoomDatabase() {
    abstract fun scoreDAO(): ScoreDAO
}