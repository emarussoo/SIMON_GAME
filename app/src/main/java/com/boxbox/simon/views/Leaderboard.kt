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
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.shape
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
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Shape
import com.boxbox.simon.ui.theme.mario
import com.boxbox.simon.ui.theme.neon
import com.boxbox.simon.ui.theme.theme
import com.boxbox.simon.ui.theme.theme3
val columnWeight = 1f

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
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                when (ThemeManager.currentTheme) {
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
                        //mette il . al posto del - e inverte in giorno/mese/anno
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
                                Modifier.weight(columnWeight),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(score.score.toString())
                            }
                            Box(
                                Modifier.weight(columnWeight),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(dayFormatted)
                            }
                            Box(
                                Modifier.weight(columnWeight),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(time.take(5))
                            }
                            Box(
                                Modifier.weight(columnWeight),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(diff)
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
                .padding(vertical = 12.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HeaderText(stringResource(R.string.scoreDB), Modifier.weight(columnWeight))
            HeaderText(stringResource(R.string.dataDB), Modifier.weight(columnWeight))
            HeaderText(stringResource(R.string.oraDB), Modifier.weight(columnWeight))
            HeaderText(stringResource(R.string.diffDB), Modifier.weight(columnWeight))
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
            HeaderText(stringResource(R.string.scoreDB), Modifier.weight(1f))
            HeaderText(stringResource(R.string.dataDB), Modifier.weight(1f))
            HeaderText(stringResource(R.string.oraDB), Modifier.weight(1f))
            HeaderText(stringResource(R.string.diffDB), Modifier.weight(1f))
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

/*@Composable
fun MarioButton(onClick: () -> Unit, text: String, modifier: Modifier) {
    val theme = ThemeManager.currentTheme

    Button(
        onClick = onClick,
        modifier
        /*modifier = Modifier
            .fillMaxWidth(0.6f)
            .height(50.dp)*/ //Modificare questa cosa anche nel tema Neon se funziona in questo
            .shadow(6.dp, RoundedCornerShape(12.dp))
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color(0xFF33BFFF),
                        Color(0xFF1A7EBF)
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .border(3.dp, Color(0xFF442F00), RoundedCornerShape(12.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black
        )
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = theme.chosenFont
        )
    }
}*/

@Composable
fun MarioButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    baseShape: Shape? = null,
    baseColor: Color? = null, // colore dinamico, se non fornito usa il colore classico
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

    // Se non è stato fornito un colore base, usiamo quelli statici
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


/*@Composable
fun NeonButton(onClick: () -> Unit, text: String, modifier: Modifier) {
    val theme = ThemeManager.currentTheme

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.6f)
            .height(50.dp)
            .shadow(10.dp, RoundedCornerShape(16.dp))
            .background(
                brush = Brush.horizontalGradient(
                    listOf(
                        Color(0xFFFFB3E6),   // rosa più chiaro e luminoso
                        Color(0xFFFE7FD4)  // colore base (rosa acceso)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .border(3.dp, Color(0xFF7A00CC), RoundedCornerShape(16.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black
        )
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = theme.chosenFont
        )
    }
}*/

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
                        Color(0xFFFFB3E6),   // rosa più chiaro e luminoso
                        Color(0xFFFE7FD4)    // colore base (rosa acceso)
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

