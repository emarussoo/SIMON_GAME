package com.boxbox.simon

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import kotlinx.coroutines.delay
import android.os.Bundle
import android.text.Layout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import com.boxbox.simon.model.Difficulty
import android.graphics.Color as AndroidColor



class MainActivity : ComponentActivity() {
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            SIMONTheme {
                screen()
                ThemeManager.switchTo2()
                }

            }
        }
    }

@Composable
fun GameScreen(viewModel: SimonViewModel, modifier: Modifier, navController: NavController){
    val context = LocalContext.current
    val state by viewModel.gameState.collectAsState()
    val timerKey by viewModel.timerKey.collectAsState()

    Box(modifier = Modifier.fillMaxSize()
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
                onEndClick = { viewModel.EndGame(context) })
            Spacer(modifier = Modifier.height(25.dp))

            ColorGrid(viewModel)
            Spacer(modifier = Modifier.height(35.dp))
        }
    }
}

@Composable
fun GameStart(viewModel: SimonViewModel){

}

@Composable
fun GameHeader(viewModel: SimonViewModel, state: SimonState, timerKey: Int, onStartClick: ()-> Unit, onEndClick:() -> Unit){
    Column(modifier = Modifier.fillMaxWidth().padding(start = 35.dp, end = 35.dp).background(color = Color.Transparent)) {
        Row(verticalAlignment = Alignment.CenterVertically)
        {
            Text(
                text = "SCORE: ",
                fontSize = 60.sp,
                color = Color.Black
            )
            Text(
                text = "${state.score}",
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold
            )

            val (buttonText, buttonColor, onClick) = when (state.state) {
                GamePhase.Idle -> Triple("start", Color.Green, onStartClick)
                GamePhase.GameOver -> Triple("start", Color.Green, onStartClick)
                GamePhase.ShowingSequence -> Triple("end", Color.Red, onEndClick)
                GamePhase.WaitingInput -> Triple("end", Color.Red, onEndClick)
            }
            TextButton(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                modifier = Modifier.padding(start = 15.dp),
            ) {
                Text(text = buttonText)
            }
        }

        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){
            val context = LocalContext.current
            val timerDuration = state.difficulty.timeDuration
            TimerProgressBar(timerKey, timerDuration, running = state.state == GamePhase.WaitingInput){ viewModel.EndGame(context) }
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

        /*Canvas(
            modifier = Modifier
                .matchParentSize()
        ) {
            val width = size.width
            val height = size.height

            val spacing = width / (segments + 1)

            for (i in 1..segments) {
                val x = spacing * i
                drawLine(
                    color = Color.White,
                    start = Offset(x, 0f),
                    end = Offset(x, height),
                    strokeWidth = 2.dp.toPx()
                )
            }
        }*/
    }
}

    /*LinearProgressIndicator(
        progress = progress.value,
        modifier = Modifier
            .fillMaxWidth()
            .height(25.dp)
            ,
        color = Color.Black,
        trackColor = Color.Transparent,

    )*/


@Composable
fun ColorGrid(viewModel: SimonViewModel){
    val context = LocalContext.current
    val highlighted by viewModel.highlightedMove.collectAsState()
    val state by viewModel.gameState.collectAsState()
    val offsetInPx = with(LocalDensity.current) { 10.dp.toPx() }

    Column(modifier = Modifier.fillMaxHeight().padding((offsetInPx.dp)/2, 0.dp, 0.dp, 0.dp).background(color = Color.Transparent), verticalArrangement = Arrangement.SpaceEvenly,horizontalAlignment = Alignment.CenterHorizontally, ){
        Row(modifier = Modifier.fillMaxWidth(),Arrangement.spacedBy(30.dp, Alignment.CenterHorizontally)){
            SimonColorButton(SimonMove.RED, highlighted == SimonMove.RED , {viewModel.onUserInput(SimonMove.RED, context)}, "right",14,R.raw.f1)
            SimonColorButton(SimonMove.GREEN, highlighted == SimonMove.GREEN , {viewModel.onUserInput(SimonMove.GREEN, context)},"right",14,R.raw.f2)
        }

        Row(modifier = Modifier.fillMaxWidth(),Arrangement.spacedBy(30.dp, Alignment.CenterHorizontally)){
            SimonColorButton(SimonMove.BLUE, highlighted == SimonMove.BLUE , {viewModel.onUserInput(SimonMove.BLUE, context)},"right",14,R.raw.bho)
            SimonColorButton(SimonMove.YELLOW, highlighted == SimonMove.YELLOW , {viewModel.onUserInput(SimonMove.YELLOW, context)},"right",14,R.raw.miao)

        }

        /*Text(text = state.state.name,
            fontSize = 10.sp, // Cambia la dimensione del testo
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center)*/

        TextButton(
            onClick = ({
                if(state.state == GamePhase.Idle || state.state == GamePhase.GameOver){
                    viewModel.setDifficulty(Difficulty.values().get(((state.difficulty.index)+1)%4))
                }else{
                }
            })
        ){
            Text(text = state.difficulty.diffName, fontSize = 30.sp)
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


@Composable
fun GameFooter(navController: NavController){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.16f).background(Color.Transparent)
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
                        .size(80.dp),
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
                        .size(80.dp),
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
                        .size(80.dp),
                    contentScale = ContentScale.FillWidth
                )
            }
    }
}

//onClick = { navController.navigate(NavigatorScreen.HowToPlay.route) }

@SuppressLint("ContextCastToActivity", "SuspiciousIndentation")
@Composable
fun GameTopper(navController: NavController) {
    val activity = (LocalContext.current as? Activity)

        Row(modifier = Modifier.padding(top = 45.dp).fillMaxHeight(0.15f).background(color = Color.Transparent).padding(top=15.dp,start = 15.dp,end=15.dp, bottom = 15.dp),
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
                    modifier = Modifier.weight(0.5f).fillMaxWidth().clickable(onClick = {navController.navigate(NavigatorScreen.HowToPlay.route)})

                )

                Image(
                    painter = painterResource(id = ThemeManager.currentTheme.quit),
                    contentDescription = "",
                    modifier = Modifier.weight(0.5f).fillMaxWidth().clickable(onClick = {activity?.finish()})
                )


            }
        }
    }



@Composable
fun SimonColorButton(move: SimonMove, highlighted: Boolean, onClick: () -> Unit, perspective: String, height: Int, sound: Int){
    val color = when(move){
        SimonMove.RED -> Color(0xffe71e07)
        SimonMove.GREEN -> Color(0xff42b033)
        SimonMove.BLUE -> Color(0xff019dda)
        SimonMove.YELLOW -> Color(0xfffcd000)
    }

    ThreeDButton(color,onClick,perspective,height,sound,highlighted)

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
    Text("SETTINGS INTERFACE")
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

        Scaffold(
            topBar = {
                if(currentRoute != "preGame")
                GameTopper(navController)
                     },
            bottomBar = {
                if(currentRoute != "preGame")
                GameFooter(navController)
                }
        ) { paddingValues ->
            Nav(
                navController = navController,
                modifier = Modifier.padding(paddingValues),
                viewModel = viewModel
            )
        }
    }


@Composable
fun preGameInterface(navController : NavController){
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize().padding(start = 15.dp, end = 15.dp),
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









