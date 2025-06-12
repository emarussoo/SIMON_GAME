package com.boxbox.simon.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.shape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.boxbox.simon.R
import com.boxbox.simon.ui.theme.ThemeManager
import com.boxbox.simon.viewmodel.LeadBoardViewModel
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import com.boxbox.simon.ui.theme.mario
import com.boxbox.simon.ui.theme.neon
import com.boxbox.simon.ui.theme.simpson

@Composable
fun LeaderboardInterface() {
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
        if (isVisible) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                when (ThemeManager.currentTheme) {
                    is mario -> MarioHeaderRow(modifier = Modifier.padding(bottom = 8.dp))
                    is neon -> NeonHeaderRow(modifier = Modifier.padding(bottom = 8.dp))
                    is simpson -> SimpsonsHeaderRow(modifier = Modifier.padding(bottom = 8.dp))
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(leaderboard) { score ->
                        val date = score.gameDate.split(" ")
                        val dayFormatted = score.gameDate.split(" ").firstOrNull()
                            ?.split("-")?.let { "${it[2]}.${it[1]}.${it[0]}" } ?: ""

                        val time = date.getOrNull(1) ?: ""
                        val diff = score.difficulty

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(
                                Modifier.weight(0.25f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(score.score.toString())
                            }
                            Box(
                                Modifier.weight(0.25f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(dayFormatted)
                            }
                            Box(
                                Modifier.weight(0.2f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(time.take(5))
                            }
                            Box(
                                Modifier.weight(0.3f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(when(diff){
                                    0 -> stringResource(R.string.easy)
                                    1-> stringResource(R.string.medium)
                                    2-> stringResource(R.string.hard)
                                    3-> stringResource(R.string.extreme)
                                    else -> "null"
                                })
                            }
                        }
                        Divider()
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                val clear = stringResource(R.string.clear_leaderboard)
                when (ThemeManager.currentTheme) {
                    is mario -> MarioButton(
                        onClick = { viewModel.resetLeaderboard(context) },
                        text = clear,
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(50.dp)
                            .background(
                                brush = Brush.verticalGradient(
                                    listOf(
                                        Color(0xFF33BFFF),
                                        Color(0xFF1A7EBF)
                                    )
                                ),
                                shape = shape
                            )
                    )

                    is neon -> NeonButton(
                        onClick = { viewModel.resetLeaderboard(context) },
                        text = clear,
                        modifier = Modifier
                    )

                    is simpson -> SimpsonsButton(
                        onClick = { viewModel.resetLeaderboard(context) },
                        text = clear,
                        modifier = Modifier
                    )
                }
            }
        }
        if (showEmptyMessage) {
            when (ThemeManager.currentTheme) {
                is mario -> MarioHeaderRow(modifier = Modifier.padding(bottom = 8.dp))
                is neon -> NeonHeaderRow(modifier = Modifier.padding(bottom = 8.dp))
                is simpson -> SimpsonsHeaderRow(modifier = Modifier.padding(bottom = 8.dp))
            }
            Box(
                modifier = Modifier.weight(1f).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.nessun_punteggio_disponibile),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }

    }
}

@Composable
fun MarioHeaderRow(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF176),
                        Color(0xFFFFC107),
                        Color(0xFFF57F17)
                    )
                )
            )
            .border(3.dp, Color(0xFF442F00), RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HeaderText(stringResource(R.string.scoreDB), Modifier.weight(0.25f))
            HeaderText(stringResource(R.string.dataDB), Modifier.weight(0.25f))
            HeaderText(stringResource(R.string.oraDB), Modifier.weight(0.20f))
            HeaderText(stringResource(R.string.diffDB), Modifier.weight(0.30f))
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
                .padding(vertical = 12.dp, horizontal = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HeaderText(stringResource(R.string.scoreDB), Modifier.weight(0.25f))
            HeaderText(stringResource(R.string.dataDB), Modifier.weight(0.25f))
            HeaderText(stringResource(R.string.oraDB), Modifier.weight(0.20f))
            HeaderText(stringResource(R.string.diffDB), Modifier.weight(0.30f))
        }
    }
}

@Composable
fun SimpsonsHeaderRow(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = Color.Black,
                spotColor = Color.Black
            )
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFFFEB3B), //Lighter yellow
                        Color(0xFFFFC107)  //Darker yellow
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 4.dp,
                color = Color.Black,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HeaderText(stringResource(R.string.scoreDB), Modifier.weight(0.25f))
            HeaderText(stringResource(R.string.dataDB), Modifier.weight(0.25f))
            HeaderText(stringResource(R.string.oraDB), Modifier.weight(0.20f))
            HeaderText(stringResource(R.string.diffDB), Modifier.weight(0.30f))
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
            fontSize = 14.4.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = theme.chosenFont,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

    }
}




@Composable
fun MarioButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    baseShape: Shape? = null,
    baseColor: Color? = null,
    textContent: @Composable () -> Unit = {
        val theme = ThemeManager.currentTheme
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = theme.chosenFont
        )
    }
) {
    val shape = if(baseShape == null){
        RoundedCornerShape(12.dp)
    }else{
        baseShape
    }

    // If a base color is not provided, static ones are used
    val brush = if (baseColor == null) {
        Brush.verticalGradient(
            listOf(
                Color(0xFF33BFFF),
                Color(0xFF1A7EBF)
            )
        )
    } else {
        Brush.verticalGradient(
            listOf(
                baseColor,
                baseColor.lighten(0.25f)
            )
        )
    }

    Button(
        onClick = onClick,
        modifier = modifier
            .shadow(6.dp, shape)
            .background(brush = brush, shape = shape)
            .border(3.dp, Color(0xFF442F00), shape),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black
        ),
        shape = shape
    ) {
        textContent()
    }
}


fun Color.lighten(factor: Float): Color {
    return Color(
        red = red + (1 - red) * factor,
        green = green + (1 - green) * factor,
        blue = blue + (1 - blue) * factor,
        alpha = alpha
    )
}


@Composable
fun SimpsonsButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    textContent: @Composable () -> Unit = {
        val theme = ThemeManager.currentTheme
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = theme.chosenFont,
            color = Color.Black
        )
    }
) {
    val shape = RoundedCornerShape(12.dp)

    Surface(
        modifier = modifier
            .shadow(elevation = 8.dp, shape = shape)
            .border(width = 3.dp, color = Color.Black, shape = shape)
            .clickable(onClick = onClick),
        shape = shape,
        color = Color.Transparent
    ) {

        Button(
            onClick = onClick,
            modifier = modifier
                .shadow(6.dp, shape)
                .background(brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF87CEEB),
                        Color(0xFFFFB6C1)
                    )
                ), shape = shape)
                .border(3.dp, Color(0xFF442F00), shape),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black
            ),
            shape = shape
        ) {
            textContent()
        }
    }
}


@Composable
fun NeonButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    textContent: @Composable () -> Unit = {
        val theme = ThemeManager.currentTheme
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = theme.chosenFont
        )
    }
) {
    val shape = RoundedCornerShape(16.dp)

    Button(
        onClick = onClick,
        modifier = modifier
            .shadow(10.dp, shape)
            .background(
                brush = Brush.horizontalGradient(
                    listOf(
                        Color(0xFFFFB3E6),   //Lighter pink
                        Color(0xFFFE7FD4)    //Darker pink
                    )
                ),
                shape = shape
            )
            .border(3.dp, Color(0xFF7A00CC), shape),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black
        ),
        shape = shape
    ) {
        textContent()
    }
}

