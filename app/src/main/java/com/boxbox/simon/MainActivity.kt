package com.boxbox.simon

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import kotlinx.coroutines.delay
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boxbox.simon.model.GamePhase
import com.boxbox.simon.model.SimonMove
import com.boxbox.simon.model.SimonState
import com.boxbox.simon.navigator.Nav
import com.boxbox.simon.ui.theme.SIMONTheme
import com.boxbox.simon.viewmodel.SimonViewModel
import com.boxbox.simon.navigator.NavigatorScreen
import com.boxbox.simon.ui.theme.ThemeManager
import com.boxbox.simon.utils.ThreeDButton
import androidx.compose.material3.*
import com.boxbox.simon.utils.playSound
import com.boxbox.simon.viewmodel.LeadBoardViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.min
import androidx.navigation.compose.currentBackStackEntryAsState
import com.boxbox.simon.model.Difficulty
import com.boxbox.simon.utils.darker
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke

import android.content.SharedPreferences
import androidx.compose.ui.res.stringResource


class MainActivity : ComponentActivity() {
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            SIMONTheme {
                screen()
                }
            }
        }
    }

@Composable
fun EndPopUp(context: Context, viewModel: SimonViewModel, onDismiss: () -> Unit = {}) {
    val state = viewModel.EndGame(context)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Game Over") },
        text = {
            Column {
                Text("Score: ${state.score}")
                Text("Difficulty: ${state.difficulty}")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}


@Composable
fun GameScreen(viewModel: SimonViewModel, modifier: Modifier, navController: NavController){
    val context = LocalContext.current
    val state by viewModel.gameState.collectAsState()
    val timerKey by viewModel.timerKey.collectAsState()

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    var showEndPopUp by remember { mutableStateOf(false) }

    if (showEndPopUp){
        EndPopUp(context, viewModel){
            showEndPopUp = false
        }
    }

    if (isLandscape) {
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
                        onEndClick = { showEndPopUp = true })

                    Spacer(modifier = Modifier.height(40.dp))

                    DifficultyAndStart(
                        viewModel,
                        state,
                        onStartClick = { viewModel.StartGame() },
                        onEndClick = { showEndPopUp = true })

                }
            }
        }


    } else {

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
                    onEndClick = { showEndPopUp = true })
                Spacer(modifier = Modifier.height(25.dp))

                ResponsiveColorGrid(viewModel)
                //Spacer(modifier = Modifier.height(35.dp))
                DifficultyAndStart(
                    viewModel,
                    state,
                    onStartClick = { viewModel.StartGame() },
                    onEndClick = {showEndPopUp = true })
            }
        }
    }
}

@Composable
fun GameStart(viewModel: SimonViewModel){

}

@Composable
fun GameHeader(viewModel: SimonViewModel, state: SimonState, timerKey: Int, onStartClick: ()-> Unit, onEndClick:() -> Unit){
    var showEndPopUp by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (showEndPopUp){
        EndPopUp(context, viewModel){
            showEndPopUp = false
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
                        if (state.state != GamePhase.GameOver){
                            showEndPopUp = true
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
fun DifficultyAndStart(viewModel: SimonViewModel, state: SimonState, onStartClick: () -> Unit, onEndClick: () -> Unit){
    ////////sezione scelta difficoltà + pulsante start/end game //////////////////////

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Level: ",
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black // opzionale, per armonizzare
            )

            Button(
                onClick = {
                    if (state.state == GamePhase.Idle || state.state == GamePhase.GameOver) {
                        viewModel.setDifficulty(
                            Difficulty.values().get((state.difficulty.index + 1) % 4)
                        )
                    }
                },
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray,
                    contentColor = Color.White,
                ),
                elevation = ButtonDefaults.run {
                    buttonElevation(
                                defaultElevation = 4.dp,
                                pressedElevation = 2.dp,
                                focusedElevation = 6.dp
                            )
                },
                modifier = Modifier
                    .padding(3.dp)
                    .height(60.dp)
                    .widthIn(min = 180.dp)
            ) {
                Text(
                    text = state.difficulty.diffName,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        //codice che precedentemente era vicino allo score//

        val (buttonText, buttonColor, onClick) = when (state.state) {
            GamePhase.Idle -> Triple("start", Color.Green.darker(), onStartClick)
            GamePhase.GameOver -> Triple("start", Color.Green.darker(), onStartClick)
            GamePhase.ShowingSequence -> Triple("end", Color.Red, onEndClick)
            GamePhase.WaitingInput -> Triple("end", Color.Red, onEndClick)
        }

        Spacer(modifier = Modifier.size(15.dp))
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(15.dp),

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

        /////////////////////////////////////////////////////////
    }
}


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ResponsiveGameFooter(navController: NavController){
    BoxWithConstraints {
        val width = maxWidth
        val imageSize = when {
            width < 360.dp -> 35.dp
            width < 480.dp -> 55.dp
            else -> 75.dp
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.16f)
                .background(Color.Transparent)
            ,
            horizontalArrangement = Arrangement.SpaceEvenly, // o SpaceBetween, Center, ecc.
            verticalAlignment = Alignment.CenterVertically
        ){

            Button(
                onClick = { navController.navigate(NavigatorScreen.Leaderboard.route) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(8.dp),
            ) {
                Image(
                    painter = painterResource(id = ThemeManager.currentTheme.cup),
                    contentDescription = "",
                    modifier = Modifier
                        .size(imageSize),
                    contentScale = ContentScale.FillBounds
                )
            }

            Button(
                onClick = { navController.navigate(NavigatorScreen.Game.route) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(8.dp),
            ) {
                Image(
                    painter = painterResource(id = ThemeManager.currentTheme.joystick),
                    contentDescription = "",
                    modifier = Modifier
                        .size(imageSize),
                    contentScale = ContentScale.FillWidth
                )
            }

            Button(
                onClick = { navController.navigate(NavigatorScreen.Settings.route) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(8.dp),
            ) {
                Image(
                    painter = painterResource(id = ThemeManager.currentTheme.settings),
                    contentDescription = "",
                    modifier = Modifier
                        .size(imageSize),
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }



    }

//onClick = { navController.navigate(NavigatorScreen.HowToPlay.route) }

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ResponsiveGameFooterLandscape(navController: NavController) {
    BoxWithConstraints {
        val height = maxHeight
        val imageSize = when {
            height < 360.dp -> 35.dp
            height < 480.dp -> 55.dp
            else -> 75.dp
        }

        Column(
            modifier = Modifier
                .fillMaxHeight() // ora la colonna occupa il 16% della larghezza, adattalo se vuoi
                .background(Color.LightGray)
                .padding(start = 15.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                onClick = { navController.navigate(NavigatorScreen.Leaderboard.route) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(8.dp),
            ) {
                Image(
                    painter = painterResource(id = ThemeManager.currentTheme.cup),
                    contentDescription = "",
                    modifier = Modifier.size(imageSize),
                    contentScale = ContentScale.FillBounds
                )
            }

            Button(
                onClick = { navController.navigate(NavigatorScreen.Game.route) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(8.dp),
            ) {
                Image(
                    painter = painterResource(id = ThemeManager.currentTheme.joystick),
                    contentDescription = "",
                    modifier = Modifier.size(imageSize),
                    contentScale = ContentScale.FillWidth
                )
            }

            Button(
                onClick = { navController.navigate(NavigatorScreen.Settings.route) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(8.dp),
            ) {
                Image(
                    painter = painterResource(id = ThemeManager.currentTheme.settings),
                    contentDescription = "",
                    modifier = Modifier.size(imageSize),
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }
}

@SuppressLint("ContextCastToActivity", "SuspiciousIndentation")
@Composable
fun GameTopper(navController: NavController) {
    val activity = (LocalContext.current as? Activity)
    var showPopUp by remember { mutableStateOf(false) }

    if (showPopUp){
        AlertDialog(
            onDismissRequest = { showPopUp = false },
            title = { Text("Uscita")},
            text = { Text("Vuoi uscire dal gioco?") },
            confirmButton = {
                TextButton(onClick = {
                    showPopUp = false
                    activity?.finishAffinity()
                }){
                    Text("Sì")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showPopUp = false
                }) {
                    Text("No")
                }
            }
        )
    }

        Row(modifier = Modifier
            .padding(top = 45.dp)
            .fillMaxHeight(0.15f)
            .background(color = Color.Transparent)
            .padding(top = 15.dp, start = 15.dp, end = 15.dp, bottom = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp))
        {

            Image(
                painter = painterResource(id = ThemeManager.currentTheme.title),
                contentDescription = "",
                modifier = Modifier
                    .weight(0.9f)
                    .fillMaxHeight()
            )

            Column(modifier = Modifier
                .weight(0.1f)
                .fillMaxHeight(),
                //verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Image(
                    painter = painterResource(id = ThemeManager.currentTheme.help),
                    contentDescription = "",
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxWidth()
                        .clickable(onClick = { navController.navigate(NavigatorScreen.HowToPlay.route) })

                )

                Image(
                    painter = painterResource(id = ThemeManager.currentTheme.quit),
                    contentDescription = "",
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxWidth()
                        .clickable(onClick = { showPopUp = true })
                )

            }
        }
    }

@SuppressLint("ContextCastToActivity", "SuspiciousIndentation")
@Composable
fun GameTopperLandscape(navController: NavController) {
    val activity = (LocalContext.current as? Activity)

    Column(modifier = Modifier
        .background(color = Color.LightGray)
        .padding(start = 40.dp, bottom = 15.dp, top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp))
    {

        Image(
            painter = painterResource(id = R.drawable.title_land),
            contentDescription = "",
            modifier = Modifier
                .weight(0.9f)
                .fillMaxHeight()
        )

        Row(modifier = Modifier
            .weight(0.1f)
            .fillMaxHeight(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = painterResource(id = ThemeManager.currentTheme.help),
                contentDescription = "",
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxWidth()
                    .clickable(onClick = { navController.navigate(NavigatorScreen.HowToPlay.route) })

            )

            Image(
                painter = painterResource(id = ThemeManager.currentTheme.quit),
                contentDescription = "",
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxWidth()
                    .clickable(onClick = { activity?.finish() })
            )

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

    Column( //Contenitore per non far sminchiare tutto
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        AnimatedVisibility(
            visible = showEmptyMessage,
            enter = fadeIn(tween(500)) + slideInVertically(
                animationSpec = tween(500),
                initialOffsetY = { fullHeight -> fullHeight / 4 }
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Nessun punteggio disponibile",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(tween(500)) + slideInVertically(
                animationSpec = tween(500),
                initialOffsetY = { fullHeight -> fullHeight / 4 }
            )
        ) {
            Column(modifier = Modifier.weight(1f)) {
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
                        Text("Score", fontWeight = FontWeight.Bold)
                    }
                    Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text("Data", fontWeight = FontWeight.Bold)
                    }
                    Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text("Ora", fontWeight = FontWeight.Bold)
                    }
                    Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text("Diff", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn {
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
                            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                Text(day)
                            }
                            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                Text(time.subSequence(0,5).toString())
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

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { viewModel.resetLeaderboard(context) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Clear Leaderboard")
        }
    }
}


@Composable
fun settingInterface(){

    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    var graphics by remember { mutableStateOf("Medium") }
    var sounds by remember { mutableStateOf(sharedPref.getBoolean("soundsOn", true)) }
    // ,false è il parametro di default se ancora non è stato messo nelle shared pref is3D
    var bttnStyle by remember { mutableStateOf(if (sharedPref.getBoolean("is3D", false)) "3D" else "Flat") }
    var theme by remember { mutableStateOf(sharedPref.getString("theme", "Standard") ?: "Standard") }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text("Settings", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(16.dp))

        Text("Language")
        Row {
            listOf("Italiano", "English", "Napoli").forEach { level ->
                Button(
                    onClick = { graphics = level },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (graphics == level) Color(0xFF1E88E5) else Color.DarkGray
                    ),
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1f)
                ) {
                    Text(level)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Music toggle
        Text("Sounds")
        Row {
            listOf("On" to true, "Off" to false).forEach { (label, value) ->
                Button(
                    onClick = {
                        sounds = value
                        sharedPref.edit().putBoolean("soundsOn", value).apply()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (sounds == value) Color(0xFF1E88E5) else Color.DarkGray
                    ),
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1f)
                ) {
                    Text(label)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Text("Button style")
        Row {
            listOf("3D", "Flat").forEach { option ->
                Button(
                    onClick = {
                        bttnStyle = option
                        if (option == "3D") {
                            sharedPref.edit().putBoolean("is3D", true).apply()
                        } else {
                            sharedPref.edit().putBoolean("is3D", false).apply()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (bttnStyle == option) Color(0xFF1E88E5) else Color.DarkGray
                    ),
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1f)
                ) {
                    Text(option)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Text("Themes")
        Row {
            listOf("IdraulicoIT", "Standard", "ScottMcTominay").forEach { option ->
                Button(
                    onClick = {
                        theme = option
                        sharedPref.edit().putString("theme", option).apply()
                        //un pò puzzolente qui
                        if(option.equals("IdraulicoIT")) ThemeManager.switchTo1()
                        else if(option.equals("Standard")) ThemeManager.switchTo2()
                        else ThemeManager.switchTo3()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (theme == option) Color(0xFF1E88E5) else Color.DarkGray
                    ),
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1f)
                ) {
                    Text(option)
                }
            }
        }
    }
}


@Composable
fun howToPlayInterface(){

    val primaryColor = Color(0xFF000000)
    val textColor = Color(0xFF212121)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 15.dp)
            .background(Color.White),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "How to Play?",
            fontSize = 60.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            color = primaryColor,
            lineHeight = 68.sp
        )

        val steps = listOf(
            "The game will show a sequence of colors and play sounds.",
            "Tap the buttons in the same order as shown.",
            "A new color is added to the sequence each turn.",
            "Game over! Try again and beat your high score."
        )

        val stepsTitle = listOf(
            "1. Watch and Listen:",
            "2. Repeat the Sequence:",
            "3. Each Round Gets Harder:",
            "4. Make a Mistake?"
        )

        stepsTitle.zip(steps).forEach { (title, desc) ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = primaryColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = desc,
                    fontSize = 20.sp,
                    lineHeight = 26.sp,
                    textAlign = TextAlign.Center,
                    color = textColor
                )
            }
        }
    }

}

@SuppressLint("SuspiciousIndentation")
@Composable
fun screen() {

    val navController = rememberNavController()
    val viewModel: SimonViewModel = viewModel()
    viewModel.setDifficulty(Difficulty.EXTREME)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {

        Row(Modifier.fillMaxSize()){
            Box(Modifier
                .weight(0.15f)
                .fillMaxHeight()) {
                if(currentRoute != "preGame") GameTopperLandscape(navController)
            }

            Box(Modifier
                .weight(0.7f)
                .fillMaxHeight()) {
                Nav(navController = navController,
                    modifier = Modifier.padding(0.dp),
                    viewModel = viewModel)
            }
            Box(Modifier
                .weight(0.15f)
                .fillMaxHeight()) {
                if(currentRoute != "preGame") ResponsiveGameFooterLandscape(navController)
            }

        }

    } else {
        Scaffold(
            topBar = {
                if(currentRoute != "preGame")
                    GameTopper(navController)
            },
            bottomBar = {
                if(currentRoute != "preGame")
                    ResponsiveGameFooter(navController)
            }
        ) { paddingValues ->
            Nav(
                navController = navController,
                modifier = Modifier.padding(paddingValues),
                viewModel = viewModel
            )
        }
    }


}


@Composable
fun preGameInterface(navController : NavController){
    val context = LocalContext.current
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(start = 15.dp, end = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){
        Image(
            painter = painterResource(id = ThemeManager.currentTheme.title),
            contentDescription = "",
            //modifier = Modifier.fillMaxSize()
        )
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = { navController.navigate(NavigatorScreen.Game.route)
                        playSound(R.raw.start,context)},
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(8.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.play_button),
                contentDescription = "",
                modifier = Modifier
                    .size(130.dp),
                contentScale = ContentScale.FillWidth
            )
        }
    }

    }









