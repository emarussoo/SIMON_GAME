package com.boxbox.simon.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.boxbox.simon.model.DB.DBAccess

import com.boxbox.simon.model.DB.ScoreEntity
import kotlinx.coroutines.launch

class LeadBoardViewModel: ViewModel(){
    private val _leaderboard = MutableStateFlow<List<ScoreEntity>>(emptyList())
    val leaderboard: StateFlow<List<ScoreEntity>> = _leaderboard.asStateFlow()

    fun loadLeaderboard(context: Context) {
        viewModelScope.launch {
            val dao = DBAccess.getDB(context).scoreDAO()
            _leaderboard.value = dao.getTop10Scores()
        }
    }

    fun resetLeaderboard(context: Context){
        viewModelScope.launch {
            val dao = DBAccess.getDB(context).scoreDAO()
            dao.deleteAllScores()
            _leaderboard.value = emptyList()
        }
    }
}