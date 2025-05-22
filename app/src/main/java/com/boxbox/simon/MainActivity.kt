package com.boxbox.simon

import android.annotation.SuppressLint
import android.app.Activity
import kotlinx.coroutines.delay
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
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
import com.boxbox.simon.ui.theme.theme
import com.boxbox.simon.ui.theme.theme2
import com.boxbox.simon.utils.ThreeDButton
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.boxbox.simon.utils.playSound


class MainActivity : ComponentActivity() {
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            SIMONTheme {
                ThemeManager.switchTo1()
                screen()
                }
            }
        }
    }

@Composable
fun GameScreen(viewModel: SimonViewModel, modifier: Modifier, navController: NavController){
    val context = LocalContext.current
    val state by viewModel.gameState.collectAsState()
    val timerKey by viewModel.timerKey.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        GameHeader(viewModel, state, timerKey, onStartClick = {viewModel.StartGame()}, onEndClick = {viewModel.EndGame(context)})
        Spacer(modifier = Modifier.height(25.dp))

        ColorGrid(viewModel)
        Spacer(modifier = Modifier.height(35.dp))
    }
}

@Composable
fun GameHeader(viewModel: SimonViewModel, state: SimonState, timerKey: Int, onStartClick: ()-> Unit, onEndClick:() -> Unit){
    val context = LocalContext.current
    Column(modifier = Modifier.padding(start = 20.dp).fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically,
            )
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
            val context = LocalContext.current
            TimerProgressBar(timerKey, running = state.state == GamePhase.WaitingInput){ viewModel.EndGame(context) }
    }
    }

@Composable
fun TimerProgressBar(
    key: Int, //cambiala e resetta
    durationMillis: Int = 2500,
    running: Boolean,
    onTimeout: () -> Unit
) {
    val progress = remember(key) { Animatable(1f) }

    if(running) {
        LaunchedEffect(key) {

            progress.snapTo(1f)
            progress.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis)
            )
            onTimeout()

        }
    }

    LinearProgressIndicator(
        progress = progress.value,
        modifier = Modifier
            .fillMaxWidth()
            .height(25.dp)
            .padding(start=10.dp,end=10.dp).clip(RoundedCornerShape(0.dp)),

        color = Color.Black,
        trackColor = Color.Transparent,
    )
}


@Composable
fun ColorGrid(viewModel: SimonViewModel){
    val context = LocalContext.current
    val highlighted by viewModel.highlightedMove.collectAsState()
    val state by viewModel.gameState.collectAsState()
    Column(modifier = Modifier.fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly){
        Row(modifier = Modifier.fillMaxWidth(),Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally)){
            SimonColorButton(SimonMove.RED, highlighted == SimonMove.RED , {viewModel.onUserInput(SimonMove.RED, context)}, "right",14,R.raw.f1)
            SimonColorButton(SimonMove.GREEN, highlighted == SimonMove.GREEN , {viewModel.onUserInput(SimonMove.GREEN, context)},"right",14,R.raw.f2)
        }

        Row(modifier = Modifier.fillMaxWidth(),Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally)){
            SimonColorButton(SimonMove.BLUE, highlighted == SimonMove.BLUE , {viewModel.onUserInput(SimonMove.BLUE, context)},"right",14,R.raw.bho)
            SimonColorButton(SimonMove.YELLOW, highlighted == SimonMove.YELLOW , {viewModel.onUserInput(SimonMove.YELLOW, context)},"right",14,R.raw.miao)

        }
        Spacer(modifier = Modifier.height(25.dp))
        Text(text = state.state.name,
            fontSize = 30.sp, // Cambia la dimensione del testo
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center)

        LaunchedEffect(state) {
            when {
                state.state.name == "GameOver" -> playSound(R.raw.lose, context)
                state.state.name == "ShowingSequence" && state.score != 0 ->{
                    delay(500L)  // ritardo di 1 secondo (1000 ms)
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
            .fillMaxHeight(0.16f)
            .border(1.dp, Color.Red)
           ,
        horizontalArrangement = Arrangement.SpaceEvenly, // o SpaceBetween, Center, ecc.
        verticalAlignment = Alignment.CenterVertically
    ){

            Button(
                onClick = { navController.navigate(NavigatorScreen.Leaderboard.route) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.border(1.dp, Color.Blue)
            ) {
                Image(
                    painter = painterResource(id = ThemeManager.currentTheme.cup),
                    contentDescription = "",
                    modifier = Modifier
                        .size(80.dp),
                    contentScale = ContentScale.FillWidth
                    )
            }

            Button(
                onClick = { navController.navigate(NavigatorScreen.Game.route) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.border(1.dp, Color.Blue)
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
                modifier = Modifier.border(1.dp, Color.Blue)
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



@SuppressLint("ContextCastToActivity", "SuspiciousIndentation")
@Composable
fun GameTopper(navController: NavController) {
    val activity = (LocalContext.current as? Activity)

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxHeight(0.20f).padding(top = 24.dp)
        ){

            Image(
                painter = painterResource(id = ThemeManager.currentTheme.title),
                contentDescription = "",
                modifier = Modifier
                    .weight(0.25f)
                    .fillMaxHeight(),
                contentScale = ContentScale.Fit
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(1.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier // qui mettiamo la modifica per il padding o larghezza
                    .wrapContentWidth() // la Column prende solo lo spazio necessario
                    .padding(end = 16.dp)
            ) {
                Button(
                    onClick = { navController.navigate(NavigatorScreen.HowToPlay.route) },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Image(
                        painter = painterResource(id = ThemeManager.currentTheme.help),
                        contentDescription = "",
                        modifier = Modifier
                            .size(40.dp),
                        contentScale = ContentScale.FillWidth
                    )
                }

                Button(
                    onClick = { activity?.finish() },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Image(
                        painter = painterResource(id = ThemeManager.currentTheme.quit),
                        contentDescription = "",
                        modifier = Modifier
                            .size(40.dp),
                        contentScale = ContentScale.FillWidth
                    )
                }
            }
        }
    }


//se metti height=0 metti i bottoni di base di emanuele
@Composable
fun SimonColorButton(move: SimonMove, highlighted: Boolean, onClick: () -> Unit, perspective: String, height: Int, sound: Int){
    val color = when(move){
        SimonMove.RED -> if(highlighted) Color.Red else Color(0xff990000)
        SimonMove.GREEN -> if(highlighted) Color.Green else Color(0xff009900)
        SimonMove.BLUE -> if(highlighted) Color(0xff3333ff) else Color(0xff000099)
        SimonMove.YELLOW -> if(highlighted) Color(0xffffff99) else Color(0xffb3b300)

    }

    if(height == 0) {
        Box(
            modifier = Modifier
                .size(175.dp)
                .padding(8.dp)
                .background(color, shape = RoundedCornerShape(16.dp))
                .clickable { onClick()}
        ) {
        }
    }
    else{
    ThreeDButton(color,onClick,perspective,height,sound)
    }

}


@Composable
fun leaderboardInterface(){
    Text("LEADERBOARD INTERFACE")

}


@Composable
fun settingInterface(){
    Text("SETTINGS INTERFACE")
}

@Composable
fun howToPlayInterface(){
    Text("HOW TO PLAY")
}

@Composable
fun screen(){
    val navController = rememberNavController()
    val viewModel: SimonViewModel = viewModel()
    Scaffold(

        topBar = { GameTopper(navController)},
        bottomBar = { GameFooter(navController) }

    ){

        Nav(
            navController = navController,
            modifier = Modifier.padding(it),
            viewModel = viewModel
        )
    }

}




