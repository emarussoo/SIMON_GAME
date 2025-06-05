package com.boxbox.simon.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import com.boxbox.simon.R
import com.boxbox.simon.model.Difficulty
import com.boxbox.simon.model.GamePhase
import com.boxbox.simon.model.SimonMove
import com.boxbox.simon.model.SimonState
import com.boxbox.simon.ui.theme.ThemeManager
import com.boxbox.simon.utils.ThreeDButton
import com.boxbox.simon.utils.darker
import com.boxbox.simon.utils.playSound
import com.boxbox.simon.viewmodel.SimonViewModel
import kotlinx.coroutines.delay

@Composable
fun GameScreen(viewModel: SimonViewModel){
    val context = LocalContext.current
    val state by viewModel.gameState.collectAsState()
    val timerKey by viewModel.timerKey.collectAsState()

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    //var showEndPopUp by remember { mutableStateOf(false) }

    if (state.state == GamePhase.GameOver){
        EndPopUp(context, viewModel){
            viewModel.resetGamePhase()
        }
    }

    if (isLandscape) LandScapeGameLayout(viewModel, state, timerKey, /*changeShowEndPopupValue = { viewModel.EndGame(context) }*/)
        else VerticalGameLayout(viewModel, state, timerKey, /*changeShowEndPopupValue = { viewModel.EndGame(context) }*/)

}


@Composable
fun LandScapeGameLayout(
    viewModel: SimonViewModel,
    state: SimonState,
    timerKey: Int,
    //changeShowEndPopupValue: (Boolean) -> Unit
){
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .background(color = Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Row(modifier = Modifier,
            horizontalArrangement = Arrangement.Center) {
            ResponsiveColorGrid(viewModel)
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                GameHeader(
                    viewModel,
                    state,
                    timerKey,
                    onStartClick = { viewModel.StartGame() },
                    onEndClick = { /*changeShowEndPopupValue(true)*/ })

                Spacer(modifier = Modifier.height(40.dp))

                DifficultyAndStart(
                    viewModel,
                    state,
                    onStartClick = { viewModel.StartGame() },
                    onEndClick = { viewModel.EndGame(context) })

            }
        }
    }
}

@Composable
fun VerticalGameLayout(
    viewModel: SimonViewModel,
    state: SimonState,
    timerKey: Int,
    //changeShowEndPopupValue: (Boolean) -> Unit
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GameHeader(
                viewModel,
                state,
                timerKey,
                onStartClick = { viewModel.StartGame() },
                onEndClick = { /*changeShowEndPopupValue(true)*/ })
            Spacer(modifier = Modifier.height(25.dp))

            ResponsiveColorGrid(viewModel)
            //Spacer(modifier = Modifier.height(35.dp))
            DifficultyAndStart(
                viewModel,
                state,
                onStartClick = { viewModel.StartGame() },
                onEndClick = { viewModel.EndGame(context)})
        }
    }
}


@Composable
fun GameHeader(viewModel: SimonViewModel, state: SimonState, timerKey: Int, onStartClick: ()-> Unit, onEndClick:() -> Unit){
    //var showEndPopUp by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (state.state == GamePhase.GameOver){
        EndPopUp(context, viewModel){
            viewModel.resetGamePhase()
        }
    }

    Column(modifier = Modifier
        //.fillMaxWidth()
        .padding(start = 35.dp, end = 35.dp)
        .background(color = Color.Transparent)) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center){

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                Text(
                    text = stringResource(R.string.score),
                    fontSize = 45.sp,
                    color = Color.Black
                )
                Text(
                    text = "${state.score}",
                    fontSize = 45.sp,
                    fontWeight = FontWeight.Bold
                )

            }
        }

        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){
            val context = LocalContext.current
            val timerDuration = state.difficulty.timeDuration
            TimerProgressBar(timerKey, timerDuration, running = state.state == GamePhase.WaitingInput){
                viewModel.EndGame(context)
            }
        }
    }
}


@Composable
fun TimerProgressBar(
    key: Int, //cambiala e resetta
    durationMillis: Int, // velocità
    running: Boolean,
    onTimeout: () -> Unit
) {
    val progress = remember(key) { Animatable(1f) }
    val animatedProgress = progress.value
    val segments = 80

    LaunchedEffect(key, running) {
        if (running) {
            progress.snapTo(1f)
            progress.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis)
            )
            onTimeout()
        } else {
            // Quando non è in esecuzione, tieni la barra piena
            progress.snapTo(1f)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .height(20.dp)
                .fillMaxWidth(animatedProgress)
                .align(Alignment.CenterStart)
                .background(Color.Black)
        )
    }
}


@Composable
fun TimerProgressBarCircle(
    key: Int,
    durationMillis: Int,
    running: Boolean,
    onTimeout: () -> Unit
) {
    val progress = remember(key) { Animatable(1f) }

    LaunchedEffect(key, running) {
        if (running) {
            progress.snapTo(1f)
            progress.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis)
            )
            onTimeout()
        } else {
            progress.snapTo(1f)
        }
    }

    val animatedProgress = progress.value

    Box(
        modifier = Modifier
            .size(120.dp)
            .padding(2.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Sfondo cerchio
            drawArc(
                color = Color.LightGray,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 15f, cap = StrokeCap.Round)
            )

            // Progresso
            drawArc(
                color = Color.Black,
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = Stroke(width = 15f, cap = StrokeCap.Round)
            )
        }
    }
}


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ResponsiveColorGrid(viewModel: SimonViewModel){

    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("settings", Context.MODE_PRIVATE)


    val highlighted by viewModel.highlightedMove.collectAsState()
    val state by viewModel.gameState.collectAsState()
    val offsetInPx = with(LocalDensity.current) { 10.dp.toPx() }
    var isEnabled = false
    val height: Int

    if(sharedPref.getBoolean("is3D",true) == true) height = 14 else height = 0

    if(state.state == GamePhase.WaitingInput){
        isEnabled = true
    }else{
        isEnabled = false
    }

    BoxWithConstraints(
        modifier = Modifier
            //.fillMaxWidth()
            .padding(16.dp) // margine esterno del layout
    ) {
        val spacing = 30.dp
        val totalSpacing = spacing * 3 // 3 spazi in una griglia 2x2
        val buttonSize = (min(maxWidth, maxHeight) - totalSpacing) / 2

        Column(
            verticalArrangement = Arrangement.spacedBy(spacing),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .padding((if (height != 0) (offsetInPx.dp) / 2 else 0.dp), 0.dp, 0.dp, 0.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                SimonColorButton(SimonMove.RED, highlighted == SimonMove.RED , {viewModel.onUserInput(SimonMove.RED, context)}, height,R.raw.f1, buttonSize, isEnabled)
                SimonColorButton(SimonMove.GREEN, highlighted == SimonMove.GREEN , {viewModel.onUserInput(SimonMove.GREEN, context)},height,R.raw.f2, buttonSize, isEnabled)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                SimonColorButton(SimonMove.BLUE, highlighted == SimonMove.BLUE , {viewModel.onUserInput(SimonMove.BLUE, context)},height,R.raw.bho, buttonSize, isEnabled)
                SimonColorButton(SimonMove.YELLOW, highlighted == SimonMove.YELLOW , {viewModel.onUserInput(SimonMove.YELLOW, context)},height,R.raw.miao, buttonSize, isEnabled)
            }

            LaunchedEffect(state) {
                when {
                    state.state.name == "GameOver" -> playSound(R.raw.lose, context)
                    state.state.name == "ShowingSequence" && state.score != 0 ->{
                        delay(250L)
                        playSound(R.raw.win, context)
                    }
                }
            }
        }
    }
}

@Composable
fun SimonColorButton(move: SimonMove, highlighted: Boolean, onClick: () -> Unit, height: Int, sound: Int, buttonSize: Dp, isEnabled: Boolean){
    val color = when(move){
        SimonMove.RED -> ThemeManager.currentTheme.Red
        SimonMove.GREEN -> ThemeManager.currentTheme.Green
        SimonMove.BLUE -> ThemeManager.currentTheme.Blue
        SimonMove.YELLOW -> ThemeManager.currentTheme.Yellow
    }

    ThreeDButton(color,onClick,height,sound,highlighted, buttonSize, isEnabled)

}


@Composable
fun DifficultyAndStart(
    viewModel: SimonViewModel,
    state: SimonState,
    onStartClick: () -> Unit,
    onEndClick: () -> Unit
) {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    // Recupera la difficoltà salvata una volta
    var difficulty by remember {
        mutableStateOf(
            Difficulty.valueOf(
                sharedPref.getString("difficulty", Difficulty.EASY.name) ?: Difficulty.EASY.name
            )
        )
    }

    // Applica al ViewModel solo se non è già quella giusta
    if (state.difficulty != difficulty && (state.state == GamePhase.Idle || state.state == GamePhase.GameOver)) {
        viewModel.setDifficulty(difficulty)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.level),
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Button(
                onClick = {
                    if (state.state == GamePhase.Idle || state.state == GamePhase.GameOver) {

                        val newDifficulty = Difficulty.values()[(difficulty.index + 1) % 4]
                        difficulty = newDifficulty
                        sharedPref.edit().putString("difficulty", newDifficulty.name).apply()
                        viewModel.setDifficulty(newDifficulty)
                    }
                },
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray,
                    contentColor = Color.White,
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 2.dp,
                    focusedElevation = 6.dp
                ),
                modifier = Modifier
                    .padding(3.dp)
                    .height(60.dp)
                    .widthIn(min = 180.dp)
            ) {
                Text(
                    text = context.getString(difficulty.diffName),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.size(15.dp))

        val (buttonText, buttonColor, onClick) = when (state.state) {
            GamePhase.Idle -> Triple(stringResource(R.string.start), Color.Green.darker(), onStartClick)
            GamePhase.GameOver -> Triple(stringResource(R.string.start), Color.Green.darker(), onStartClick)
            GamePhase.ShowingSequence -> Triple(stringResource(R.string.end), Color.Red, onEndClick)
            GamePhase.WaitingInput -> Triple(stringResource(R.string.end), Color.Red, onEndClick)
        }

        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(0.dp),
            modifier = Modifier
                .padding(start = 15.dp)
                .height(50.dp)
        ) {
            Text(
                text = buttonText,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

