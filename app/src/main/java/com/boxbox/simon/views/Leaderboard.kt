package com.boxbox.simon.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.boxbox.simon.R
import com.boxbox.simon.ui.theme.ThemeManager
import com.boxbox.simon.viewmodel.LeadBoardViewModel
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.draw.shadow
import com.boxbox.simon.ui.theme.mario
import com.boxbox.simon.ui.theme.neon
import com.boxbox.simon.ui.theme.theme3

@Composable
fun leaderboardInterface() {
    val context = LocalContext.current
    val viewModel: LeadBoardViewModel = viewModel()
    val leaderboard by viewModel.leaderboard.collectAsState()

    var isVisible by remember { mutableStateOf(false) }
    var showEmptyMessage by remember { mutableStateOf(false) }

    val theme = ThemeManager.currentTheme

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
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.nessun_punteggio_disponibile),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }

        if (isVisible) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                when(ThemeManager.currentTheme){
                    is mario -> MarioHeaderRow(modifier = Modifier.padding(bottom = 8.dp))
                    is neon -> NeonHeaderRow(modifier = Modifier.padding(bottom = 8.dp))
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
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
                                Text(time.take(5))
                            }
                            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                Text(diff)
                            }
                        }
                        Divider()
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { viewModel.resetLeaderboard(context) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = theme.buttonBackground,
                        contentColor = theme.buttonTextColor
                    )
                ) {
                    Text(stringResource(R.string.clear_leaderboard))
                }
            }
        }
    }
}

@Composable
fun MarioHeaderRow(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFFFE95B), Color(0xFFF4A300))
                ),
                shape = RoundedCornerShape(8.dp)
            )
            .border(3.dp, Color(0xFF442F00), RoundedCornerShape(8.dp))
            .shadow(4.dp, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HeaderText(stringResource(R.string.scoreDB), Modifier.weight(1f))
            HeaderText(stringResource(R.string.dataDB), Modifier.weight(1f))
            HeaderText(stringResource(R.string.oraDB), Modifier.weight(1f))
            HeaderText(stringResource(R.string.diffDB), Modifier.weight(1f))
        }
    }
}

@Composable
fun NeonHeaderRow(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .shadow(12.dp, RoundedCornerShape(12.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF00BFFF),
                        Color(0xFF7DF9FF)
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .border(4.dp, Color(0xFF7A00CC), RoundedCornerShape(12.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HeaderText("Score", Modifier.weight(1f))
            HeaderText("Date", Modifier.weight(1f))
            HeaderText("Time", Modifier.weight(1f))
            HeaderText("Level", Modifier.weight(1f))
        }
    }
}

@Composable
fun HeaderText(text: String, modifier: Modifier) {
    val theme = ThemeManager.currentTheme

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(
            text = text,
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = theme.chosenFont,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}
