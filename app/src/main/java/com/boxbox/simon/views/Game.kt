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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
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
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import com.boxbox.simon.R
import com.boxbox.simon.model.Difficulty
import com.boxbox.simon.model.GamePhase
import com.boxbox.simon.model.SimonMove
import com.boxbox.simon.model.SimonState
import com.boxbox.simon.ui.theme.ThemeManager
import com.boxbox.simon.button.ThreeDButton
import com.boxbox.simon.button.playSound
import com.boxbox.simon.viewmodel.SimonViewModel
import kotlinx.coroutines.delay
import com.boxbox.simon.ui.theme.GreenForest
import com.boxbox.simon.ui.theme.Orange
import com.boxbox.simon.ui.theme.mario
import com.boxbox.simon.ui.theme.neon
import com.boxbox.simon.ui.theme.simpson
import com.boxbox.simon.ui.theme.theme

//Returns the device width
@Composable
fun GetDeviceWidth(): Int {
    val configuration = LocalConfiguration.current
    if(configuration.screenHeightDp < configuration.screenWidthDp){
        return configuration.screenHeightDp
    }else{
        return configuration.screenWidthDp
    }
}

//It manages the game page
@SuppressLint("ContextCastToActivity")
@Composable
fun GameScreen(viewModel: SimonViewModel){
    val context = LocalContext.current
    val state by viewModel.gameState.collectAsState()
    val timerKey by viewModel.timerKey.collectAsState()

    //get the current phone orientation
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    //used for showing the popup
    var showExitPopUp = remember { mutableStateOf(false) }
    val activity = (LocalContext.current as? Activity)

    //handler back gesture, it is managed only here
    //elsewhere back gestures brings user back to previous screen
    BackHandler {
        showExitPopUp.value = true
    }

    //endgame popup starter, it only starts in a certain state
    if (state.state == GamePhase.GameOver){
        EndPopUp(context, viewModel){
            viewModel.resetGamePhase()
        }
    }

    //a different layout will be loaded based on device orientation
    if (isLandscape) LandScapeGameLayout(viewModel, state, timerKey, /*changeShowEndPopupValue = { viewModel.EndGame(context) }*/)
    else VerticalGameLayout(viewModel, state, timerKey, /*changeShowEndPopupValue = { viewModel.EndGame(context) }*/)


    //exit popup starter
    if(showExitPopUp.value){
        ExitPopUp(showExitPopUp, activity)
    }
}


//This function will be used only if device orientation is landscape
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

            //4 game buttons on the left
            ResponsiveColorGrid(viewModel)

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Spacer(modifier = Modifier.height(40.dp))

                // game header with score and timebar
                GameHeader(
                    viewModel,
                    state,
                    timerKey,
                    onStartClick = { viewModel.StartGame() },
                    onEndClick = { }
                )

                Spacer(modifier = Modifier.height(10.dp))

                //difficulty and start buttons
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


//This function will be used only if device orientation is vertical
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
                onEndClick = { })

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


//Used to assemble display score and timer bar
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

                    //SCORE text
                    Text(
                        text = stringResource(R.string.score),
                        fontSize = fontSize,
                        color = Color.Black,

                        )

                    //score value text
                    Text(
                        text = "${state.score}",
                        fontSize = fontSize,
                        fontWeight = FontWeight.Bold
                    )

                }
            }

            //progress bar
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


//Used to display timer bar
//The timer restarts every time the key changes
@Composable
fun TimerProgressBar(
    key: Int, //change to reset
    durationMillis: Int, //used to choose the turn time
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
            //When is not in execution, keep the bar full
            progress.snapTo(1f)
        }
    }

    //the color changes based on the current theme of the app
    //the "lerp" function manages the fades of the colors
    val barColor = when(ThemeManager.currentTheme) {

        //mario theme colors
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

        //neon theme colors
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

        //standard option
        //used also for bart theme
        else -> Color.Black
    }

    //timer bar itself
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


//Used to display game buttons in a certain layout
//it is responsive, so this function is used in every layout(landscape/vertical) and adapts the buttons' size with the phone size
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

    //used to determine the style of the buttons (3d or not)
    if(sharedPref.getBoolean("is3D",true) == true) height = 14 else height = 0


    //used to determine the activation of the buttons
    if(state.state != GamePhase.ShowingSequence) isEnabled = true
    else isEnabled = false


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

//Used to display the buttons
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

//Used to manage the difficulty changer and the start/end button
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

    // Retrieve the difficulty from sharedpref
    var difficulty by remember {
        mutableStateOf(
            Difficulty.valueOf(
                sharedPref.getString("difficulty", Difficulty.EASY.name) ?: Difficulty.EASY.name
            )
        )
    }

    //apply the changes at viewmodel when difficulty is changed
    if (state.difficulty != difficulty && (state.state == GamePhase.Idle || state.state == GamePhase.GameOver)) {
        viewModel.setDifficulty(difficulty)
    }

    //This box is used to decide which layout will be used
    BoxWithConstraints(modifier = Modifier.border(width = 1.dp, color = Color.Transparent)){

        //this checks the box ratio, in order to use different layouts
        if((maxWidth/maxHeight) > 2.7f ){

            //row layout box
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
            //column layout box
            BoxWithConstraints(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = if (isLandscape) 3.dp else 40.dp)
                    .border(width = 1.dp, color = Color.Transparent)
            ) {

                val boxHeight = maxWidth * 0.58f
                val boxWidth = maxWidth

                Column(
                    modifier = Modifier.background(Color.Transparent)
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

                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(boxHeight * 0.5f)
                            .background(Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        val side = min(maxWidth, maxHeight) * 0.9f

                        ThemedStartStopButton(
                            onClick,
                            state,
                            "play/start",
                            theme,
                            modifier = Modifier
                                .size(side)
                                .shadow(
                                    elevation = 0.dp,
                                    shape = CircleShape,
                                    clip = false
                                )
                        )
                    }

                }
            }
        }
    }
}



//start/stop button
//it changes its style based on current theme
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


//button used to change the difficulty of the game
//it changes its style based on current theme
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
        //mario theme
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


        //neon theme
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


        //bart theme
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
    step: Float = 0.8f,
    fontWeight: FontWeight = FontWeight.Normal,
    button: Boolean
) {
    val textMeasurer = rememberTextMeasurer()
    var currentSize by remember { mutableStateOf(maxTextSize) }
    var measured by remember { mutableStateOf(false) }
    val fontFamily = MaterialTheme.typography.bodyLarge.fontFamily

    // Reference list of strings used to determine max fitting font size
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

    // Measure available width for the text
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val availableWidth = constraints.maxWidth.toFloat()

        // Dynamically adjust text size to fit the widest fixed string
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

        // Draw the text once size is determined
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