package com.boxbox.simon.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.boxbox.simon.R
import com.boxbox.simon.viewmodel.LeadBoardViewModel

@Composable
fun leaderboardInterface() {
    val context = LocalContext.current
    val viewModel: LeadBoardViewModel = viewModel()
    val leaderboard by viewModel.leaderboard.collectAsState()

    var isVisible by remember { mutableStateOf(false) }
    var showEmptyMessage by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadLeaderboard(context)
    }

    LaunchedEffect(leaderboard) {
        isVisible = leaderboard.isNotEmpty()
        showEmptyMessage = leaderboard.isEmpty()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
    ) {

        if (showEmptyMessage) {
            // Messaggio "nessun punteggio disponibile" occupa tutto lo spazio tranne il bottone
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(R.string.nessun_punteggio_disponibile),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        if (isVisible) {
            Column(
                modifier = Modifier
                    .weight(1f)  // prende tutto lo spazio disponibile sopra il bottone
            ) {
                // Header tabella
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .background(Color.White)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text(stringResource(R.string.scoreDB), fontWeight = FontWeight.Bold)
                    }
                    Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text(stringResource(R.string.dataDB), fontWeight = FontWeight.Bold)
                    }
                    Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text(stringResource(R.string.oraDB), fontWeight = FontWeight.Bold)
                    }
                    Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text(stringResource(R.string.diffDB), fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Lista scrollabile
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(leaderboard) { score ->
                        val date = score.gameDate.split(" ")
                        val day = date.getOrNull(0) ?: ""
                        val time = date.getOrNull(1) ?: ""
                        val diff = score.difficulty

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                Text(score.score.toString())
                            }
                            Box(Modifier.weight(1.1f), contentAlignment = Alignment.Center) {
                                Text(day)
                            }
                            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                Text(time.take(5)) // primi 5 caratteri (hh:mm)
                            }
                            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                Text(diff)
                            }
                        }
                        Divider()
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { viewModel.resetLeaderboard(context) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(stringResource(R.string.clear_leaderboard))
        }
    }
}
