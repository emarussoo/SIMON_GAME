package com.boxbox.simon.views

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
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

import androidx.compose.ui.text.font.FontFamily
import com.boxbox.simon.ui.theme.neon

@Composable
fun GetDeviceWidth(): Int {
    val configuration = LocalConfiguration.current
    if(configuration.screenHeightDp < configuration.screenWidthDp){
        return configuration.screenHeightDp
    }else{
        return configuration.screenWidthDp
    }
}
@SuppressLint("ContextCastToActivity")
@Composable
fun GameScreen(viewModel: SimonViewModel){
    val context = LocalContext.current
    val state by viewModel.gameState.collectAsState()
    val timerKey by viewModel.timerKey.collectAsState()

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    var showExitPopUp = remember { mutableStateOf(false) }
    val activity = (LocalContext.current as? Activity)

    BackHandler {
        showExitPopUp.value = true
    }

    if (state.state == GamePhase.GameOver){
        EndPopUp(context, viewModel){
            viewModel.resetGamePhase()
        }
    }

    if (isLandscape) LandScapeGameLayout(viewModel, state, timerKey, /*changeShowEndPopupValue = { viewModel.EndGame(context) }*/)
    else VerticalGameLayout(viewModel, state, timerKey, /*changeShowEndPopupValue = { viewModel.EndGame(context) }*/)

    if(showExitPopUp.value){
        ExitPopUp(showExitPopUp, activity)
    }
}


@Composable
fun LandScapeGameLayout(
    viewModel: SimonViewModel,
    state: SimonState,
    timerKey: Int,
){
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .background(color = Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Row(modifier = Modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
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


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun GameHeader(viewModel: SimonViewModel, state: SimonState, timerKey: Int, onStartClick: ()-> Unit, onEndClick:() -> Unit){
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (state.state == GamePhase.GameOver){
        EndPopUp(context, viewModel){
            viewModel.resetGamePhase()
        }
    }

    BoxWithConstraints(modifier = Modifier.background(Color.Transparent)) {
        val width = GetDeviceWidth()
        val padd = if (isLandscape) 3.dp else 35.dp
        val divider = if (isLandscape) 10f else 7.5f // precedentemente 5f e 7.5f
        //val fontSize = (width.value / divider).sp

        val fontSize = when {
            width < 360 -> 20.sp
            width < 400 -> 35.sp
            else -> (width / divider).sp
        }

        Column(
            modifier = Modifier
                .padding(start = padd, end = padd)
                .background(color = Color.Transparent)

        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {


                    Text(
                        text = stringResource(R.string.score),
                        fontSize = fontSize,
                        color = Color.Black,

                    )


                    Text(
                        text = "${state.score}",
                        fontSize = fontSize,
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

    if(state.state != GamePhase.ShowingSequence){
        isEnabled = true
    }else{
        isEnabled = false
    }

    BoxWithConstraints(
        modifier = Modifier
            //.fillMaxWidth()
            .padding(16.dp) // margine esterno del layout
    ) {
        val width= GetDeviceWidth()

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        val spacing = 30.dp
        val totalSpacing = spacing * 3 // 3 spazi in una griglia 2x2
        val buttonSize = when {
            width < 360 -> 60.dp
            width < 380 -> 70.dp
            else -> (min(maxWidth, maxHeight) - totalSpacing) / 2
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(spacing),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .padding((if (height != 0) (offsetInPx.dp) / 2 else 0.dp), 0.dp, 0.dp, 0.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                SimonColorButton(SimonMove.RED, highlighted == SimonMove.RED , {viewModel.onUserInput(SimonMove.RED, context)}, height,ThemeManager.currentTheme.click1Sound, buttonSize, isEnabled)
                SimonColorButton(SimonMove.GREEN, highlighted == SimonMove.GREEN , {viewModel.onUserInput(SimonMove.GREEN, context)},height,ThemeManager.currentTheme.click2Sound, buttonSize, isEnabled)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                SimonColorButton(SimonMove.BLUE, highlighted == SimonMove.BLUE , {viewModel.onUserInput(SimonMove.BLUE, context)},height,ThemeManager.currentTheme.click3Sound, buttonSize, isEnabled)
                SimonColorButton(SimonMove.YELLOW, highlighted == SimonMove.YELLOW , {viewModel.onUserInput(SimonMove.YELLOW, context)},height,ThemeManager.currentTheme.click4Sound, buttonSize, isEnabled)
            }

            LaunchedEffect(state) {
                when {
                    state.state.name == "GameOver" -> playSound(ThemeManager.currentTheme.loseSound, context)
                    state.state.name == "ShowingSequence" && state.score != 0 ->{
                        delay(250L)
                        playSound(ThemeManager.currentTheme.winSound, context)
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


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun DifficultyAndStart(
    viewModel: SimonViewModel,
    state: SimonState,
    onStartClick: () -> Unit,
    onEndClick: () -> Unit
) {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

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

    BoxWithConstraints{
        val width = GetDeviceWidth()
        val padd = if (isLandscape) 1.dp else 0.dp
        val divider = if (isLandscape) 17f else 15f
        //val fontSize = (width.value / divider).sp
        //val fontSize = 10.sp
        val fontSize = when {
            width < 360 -> 6.sp
            width < 400 -> 15.sp
            else -> (width / divider).sp
        }




        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.background(Color.Transparent).fillMaxHeight(if(isLandscape){0.5f}else{1f}).fillMaxWidth(if(isLandscape){1f}else{0.7f})
        ) {
            //Spacer(modifier = Modifier.size(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxHeight(if(isLandscape){0.35f}else{0.35f}).border(1.dp, Color.Transparent)
            ) {

                AutoResizingText(text = stringResource(R.string.level), modifier = Modifier.weight(0.5f))


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
                        .padding(padd).weight(0.5f)
                        .fillMaxHeight(1f)
                        .fillMaxWidth(0.8f)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        AutoResizingText(
                            text = context.getString(difficulty.diffName),
                        )
                    }
                }


            }

            Spacer(
                modifier = Modifier.size(
                    when {
                        width < 400 -> 10.dp
                        else -> 25.dp
                    }
                )
            )

            val (buttonText, buttonColor, onClick) = when (state.state) {
                GamePhase.Idle -> Triple(
                    stringResource(R.string.start),
                    Color.Green.darker(),
                    onStartClick
                )

                GamePhase.GameOver -> Triple(
                    stringResource(R.string.start),
                    Color.Green.darker(),
                    onStartClick
                )

                GamePhase.ShowingSequence -> Triple(
                    stringResource(R.string.end),
                    Color.Red,
                    onEndClick
                )

                GamePhase.WaitingInput -> Triple(
                    stringResource(R.string.end),
                    Color.Red,
                    onEndClick
                )
            }

            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier
                    .padding(start = padd)
                    .fillMaxWidth(0.5f)
            ) {

             AutoResizingText(text = buttonText)
            }
        }
    }
}



@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun AutoResizingText(
    text: String,
    maxTextSize: TextUnit = 100.sp,
    minTextSize: TextUnit = 1.sp,
    modifier: Modifier = Modifier,
    step: Float = 0.9f,
) {
    val textMeasurer = rememberTextMeasurer()
    var currentSize by remember { mutableStateOf(maxTextSize) }
    var measured by remember { mutableStateOf(false) }

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val availableWidth = constraints.maxWidth.toFloat()

        LaunchedEffect(availableWidth, text) {
            var size = maxTextSize
            while (size > minTextSize) {
                val result = textMeasurer.measure(
                    text = "extreme",
                    style = TextStyle(fontSize = size),
                    maxLines = 1
                )
                if (result.size.width <= availableWidth) {
                    break
                }
                size *= step
            }
            currentSize = size
            measured = true
        }

        if (measured) {
            Text(
                /*fontFamily = when(ThemeManager.currentTheme) {
                    is com.boxbox.simon.ui.theme.neon -> FontFamily(Font(R.font.neon))
                    is com.boxbox.simon.ui.theme.mario -> FontFamily(Font(R.font.supermario))
                    else -> FontFamily.Default
                },*/
                    text = text,
                    fontSize = currentSize,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                        )
                }
        }
    }