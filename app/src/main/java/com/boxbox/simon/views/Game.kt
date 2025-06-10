package com.boxbox.simon.views


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.boxbox.simon.R
import com.boxbox.simon.model.Difficulty
import com.boxbox.simon.model.GamePhase
import com.boxbox.simon.model.SimonMove
import com.boxbox.simon.model.SimonState
import com.boxbox.simon.ui.theme.ThemeManager
import com.boxbox.simon.utils.ThreeDButton
import com.boxbox.simon.utils.playSound
import com.boxbox.simon.viewmodel.SimonViewModel
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.lerp
import com.boxbox.simon.ui.theme.GreenForest
import com.boxbox.simon.ui.theme.Orange
import com.boxbox.simon.ui.theme.mario
import com.boxbox.simon.ui.theme.neon
import com.boxbox.simon.ui.theme.simpson
import com.boxbox.simon.ui.theme.theme



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

    ) {
        Row(modifier = Modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically)
        {

            ResponsiveColorGrid(viewModel)

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Spacer(modifier = Modifier.height(40.dp))
                    GameHeader(
                        viewModel,
                        state,
                        timerKey,
                        onStartClick = { viewModel.StartGame() },
                        onEndClick = { }
                    )



                    Spacer(modifier = Modifier.height(10.dp))

                    DifficultyAndStart(
                        viewModel,
                        state,
                        onStartClick = { viewModel.StartGame() },
                        onEndClick = { viewModel.EndGame(context) }
                    )


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
        modifier = Modifier.fillMaxSize().background(Color.Transparent)
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
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE


    BoxWithConstraints(modifier = Modifier.background(Color.Transparent)) {
        val width = GetDeviceWidth()
        val padd = if (isLandscape) 3.dp else 35.dp
        val divider = if (isLandscape) 10f else 7.5f


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


    LaunchedEffect(key, running) {
        if (running) {
            progress.snapTo(1f)
            progress.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis, easing = LinearEasing)
            )
            onTimeout()
        } else {
            // Quando non è in esecuzione, tieni la barra piena
            progress.snapTo(1f)
        }
    }

    val barColor = when(ThemeManager.currentTheme) {
        is mario -> when {
            animatedProgress > 0.5f -> lerp(
                Orange,
                GreenForest,
                (animatedProgress - 0.5f) * 2
            )

            else -> lerp(
                Color.Red,
                Orange,
                animatedProgress * 2
            )
        }

        is neon -> when {
            animatedProgress > 0.5f -> lerp(
                ThemeManager.currentTheme.Yellow,
                ThemeManager.currentTheme.Blue,
                (animatedProgress - 0.5f) * 2
            )

            else -> lerp(
                ThemeManager.currentTheme.Red,
                ThemeManager.currentTheme.Green,
                animatedProgress * 2
            )
        }

        else -> Color.Black
    }

                Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .border(3.dp, Color.Black)
                    .padding(2.dp)
                ) {
            Box(
                modifier = Modifier
                    .height(20.dp)
                    .fillMaxWidth(animatedProgress)
                    .align(Alignment.CenterStart)
                    .background(barColor)
            )
        }
    }


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ResponsiveColorGrid(viewModel: SimonViewModel){

    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    val configuration = LocalConfiguration.current

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
           .wrapContentSize()
            .padding(16.dp).background(Color.Transparent)
    ) {


        val spacing = 30.dp
        val totalSpacing = spacing * 3

        val buttonSize = if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) (maxWidth-totalSpacing)/3.5f
        else (maxWidth-totalSpacing)/2

        Column(
            verticalArrangement = Arrangement.spacedBy(spacing),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .padding((if (height != 0) (offsetInPx.dp) / 2 else 0.dp), 0.dp, 0.dp, 0.dp).wrapContentSize().background(Color.Transparent)
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
    val theme = ThemeManager.currentTheme
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

    //box usato per verificare quale layout di difficultyAndStart usare
    BoxWithConstraints(modifier = Modifier.fillMaxSize().border(width = 1.dp, color = Color.Transparent)){

    //controllo per vedere se le proporzioni del box permettono di usare il layout in colonna
    if((maxWidth/maxHeight) > 2.7f ){

    //box per il layout in riga
    BoxWithConstraints(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).border(width = 1.dp, color = Color.Transparent)){

        val boxWidth = maxWidth
        val boxHeight = maxWidth * 0.2f
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxHeight(if (isLandscape) 0.33f else 1f)
                    .fillMaxWidth()
            ) {

                Box(
                    modifier = Modifier
                        .width(boxWidth * 0.4f).fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    AutoResizingText(
                        text = stringResource(R.string.level),
                        button = false
                    )
                }

                Box(modifier = Modifier
                    .width(boxWidth * 0.4f).height(boxHeight).padding(end = 10.dp),
                    contentAlignment = Alignment.Center) {
                    DifficultyThemeButton(
                        text = difficulty.name,
                        difficulty = difficulty,
                        onDifficultyChange = { newDifficulty ->
                            if (state.state == GamePhase.Idle || state.state == GamePhase.GameOver) {
                                difficulty = newDifficulty
                                sharedPref.edit().putString("difficulty", newDifficulty.name).apply()
                                viewModel.setDifficulty(newDifficulty)
                            }
                        },
                        modifier = Modifier
                            .fillMaxHeight()
                    )

                }
                val onClick = when (state.state) {
                    GamePhase.Idle, GamePhase.GameOver -> onStartClick
                    GamePhase.ShowingSequence, GamePhase.WaitingInput -> onEndClick
                }

                //Spacer(modifier = Modifier.width(10.dp))
                Box(modifier = Modifier
                    .width(boxWidth * 0.2f).fillMaxSize(),
                    contentAlignment = Alignment.Center) {
                    ThemedStartStopButton(onClick, state, "play/start", theme, modifier = Modifier.size(boxHeight)
                    )
                }
            }


        }



    }
    else {
        //box per il layout in colonna
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = if (isLandscape) 3.dp else 40.dp)
                .border(width = 1.dp, color = Color.Transparent)
        ) {

            val boxHeight = maxWidth * 0.58f
            val boxWidth = maxWidth

            Column(
                modifier = Modifier.fillMaxSize().background(Color.Transparent)
                    .border(width = 1.dp, color = Color.Transparent),
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth().height(boxHeight * 0.5f)
                        .border(1.dp, Color.Transparent),
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxHeight().width(boxWidth * 0.5f)
                            .background(Color.Transparent)
                    ) {
                        AutoResizingText(
                            text = stringResource(R.string.level),
                            modifier = Modifier.background(Color.Transparent),
                            button = false
                        )
                    }
                    Box(modifier = Modifier.padding(vertical = 10.dp)) {
                        DifficultyThemeButton(
                            text = difficulty.name,
                            difficulty = difficulty,
                            onDifficultyChange = { newDifficulty ->
                                if (state.state == GamePhase.Idle || state.state == GamePhase.GameOver) {
                                    difficulty = newDifficulty
                                    sharedPref.edit().putString("difficulty", newDifficulty.name)
                                        .apply()
                                    viewModel.setDifficulty(newDifficulty)
                                }
                            },
                            modifier = Modifier.fillMaxHeight().width(boxWidth * 0.5f)
                        )
                    }
                }


                val onClick = when (state.state) {
                    GamePhase.Idle, GamePhase.GameOver -> onStartClick
                    GamePhase.ShowingSequence, GamePhase.WaitingInput -> onEndClick
                }

                Box(
                    modifier = Modifier.fillMaxWidth().height(boxHeight * 0.5f)
                        .background(Color.Transparent), contentAlignment = Alignment.Center
                ) {
                    ThemedStartStopButton(
                        onClick,
                        state,
                        "play/start",
                        theme,
                        modifier = Modifier.size(boxHeight * 0.45f)
                    )
                }
            }
        }
    }
    }
    }



@Composable
fun ThemedStartStopButton(
    onClick: () -> Unit,
    state: SimonState,
    buttonText: String,
    theme: theme,
    modifier: Modifier
){
    val (containerColor, contentColor) = when(theme){
        is mario -> Color.Black to Color.White
        is neon -> Color(0xFF7A00CC) to Color.White
        is simpson -> Color(0xff65C03E) to Color.White
        else -> Color.Black to Color.White
    }

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
    ) {
        Icon(
            imageVector = if (state.state == GamePhase.Idle || state.state == GamePhase.GameOver)
                Icons.Default.PlayArrow
            else
                Icons.Default.Close,
            contentDescription = buttonText,
            modifier = Modifier.fillMaxSize().padding(10.dp)
        )
    }

}


@Composable
fun DifficultyThemeButton(
    text: String,
    difficulty: Difficulty,
    onDifficultyChange: (Difficulty) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val theme = ThemeManager.currentTheme
    val backColor = theme.difficultyColors[difficulty] ?: Color.DarkGray
    val onClick = {
        val newDifficulty = Difficulty.values()[(difficulty.index + 1) % Difficulty.values().size]
        onDifficultyChange(newDifficulty)
    }

    when (theme) {
        is mario -> MarioButton(
            onClick = onClick,
            text = text,
            modifier = modifier.height(50.dp),
            baseColor = backColor,
            baseShape = RoundedCornerShape(30.dp),
            textContent = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    AutoResizingText(
                        text = context.getString(difficulty.diffName), button = true
                    )
                }
            }
        )

        is neon -> NeonButton(
            onClick = onClick,
            text = text,
            modifier = modifier,
            textContent = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    AutoResizingText(
                        text = context.getString(difficulty.diffName),button = true
                    )
                }
            }
        )

        is simpson -> SimpsonsButton(
            onClick = onClick,
            text = text,
            modifier = modifier,
            textContent = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    AutoResizingText(
                        text = context.getString(difficulty.diffName),button = true
                    )
                }
            }
        )
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
    fontWeight: FontWeight = FontWeight.Normal,
    button: Boolean
) {
    val textMeasurer = rememberTextMeasurer()
    var currentSize by remember { mutableStateOf(maxTextSize) }
    var measured by remember { mutableStateOf(false) }
    val fontFamily = MaterialTheme.typography.bodyLarge.fontFamily

    // Lista fissa di stringhe da confrontare
    val fixedTexts = if (button == true) {
        listOf(
            stringResource(R.string.easy),
            stringResource(R.string.extreme),
            stringResource(R.string.hard),
            stringResource(R.string.medium),
        )
    } else {
        listOf(stringResource(R.string.level))
    }

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val availableWidth = constraints.maxWidth.toFloat()

        LaunchedEffect(availableWidth) {
            var size = maxTextSize
            while (size > minTextSize) {
                val allFit = fixedTexts.all { text ->
                    val result = textMeasurer.measure(
                        text = text,
                        style = TextStyle(fontSize = size, fontFamily = fontFamily),
                        maxLines = 1
                    )
                    result.size.width <= availableWidth
                }

                if (allFit) break
                size *= step
            }
            currentSize = size
            measured = true
        }

        if (measured) {
            Text(
                text = text,
                fontSize = currentSize,
                fontWeight = fontWeight,
                fontFamily = fontFamily,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                softWrap = false,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(0.dp),
            )
        }
    }
}

