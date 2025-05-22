package com.boxbox.simon.model.DB

import android.content.Context
import androidx.room.Room

object DBAccess {
    private var instance: SimonDB? = null

    fun getDB(context: Context): SimonDB {
        if(instance == null){
            instance = Room.databaseBuilder(
                context.applicationContext,
                SimonDB::class.java,
                "Simon_DB"
            ).build()
        }
        return instance!!
    }
}