package com.boxbox.simon

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
fun GameScreen(viewModel: SimonViewModel, modifier: Modifier, navController: NavController){
    val state by viewModel.gameState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        GameHeader(state, onStartClick = {viewModel.StartGame()}, onEndClick = {viewModel.EndGame()})
        Spacer(modifier = Modifier.height(30.dp))

        ColorGrid(viewModel)
        Spacer(modifier = Modifier.height(35.dp))
    }
}

@Composable
fun GameHeader(state: SimonState, onStartClick: ()-> Unit, onEndClick:() -> Unit){
    Column(horizontalAlignment = Alignment.CenterHorizontally){
        Text("SCORE: ${state.score}", fontSize = 24.sp, modifier = Modifier.border(1.dp, Color.Red))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(16.dp)
                .border(1.dp, Color.Red)

        ){
            val (buttonText, buttonColor, onClick) = when(state.state){
                GamePhase.Idle -> Triple("Inizia partita", Color.Green, onStartClick)
                GamePhase.GameOver -> Triple("Inizia partita", Color.Green, onStartClick)
                GamePhase.ShowingSequence -> Triple("Termina partita", Color.Red, onEndClick)
                GamePhase.WaitingInput -> Triple("Termina partita", Color.Red, onEndClick)
            }
            TextButton(onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor)) {
                Text(buttonText)
            }
            Text("|||||||||||||Tempo||||||||||||||||||", modifier = Modifier.border(1.dp, Color.Red))
        }

    }
}

@Composable
fun ColorGrid(viewModel: SimonViewModel){
    val highlighted by viewModel.highlightedMove.collectAsState()
    val state by viewModel.gameState.collectAsState()
    Column(modifier = Modifier
        .border(1.dp, Color.Red)
        .background(Color.Gray)){
        Row{
            SimonColorButton(SimonMove.RED, highlighted == SimonMove.RED , {viewModel.onUserInput(SimonMove.RED)})
            SimonColorButton(SimonMove.GREEN, highlighted == SimonMove.GREEN , {viewModel.onUserInput(SimonMove.GREEN)})

        }
        Row{
            SimonColorButton(SimonMove.BLUE, highlighted == SimonMove.BLUE , {viewModel.onUserInput(SimonMove.BLUE)})
            SimonColorButton(SimonMove.YELLOW, highlighted == SimonMove.YELLOW , {viewModel.onUserInput(SimonMove.YELLOW)})

        }

        Text(state.state.name)
    }
}


@Composable
fun GameFooter(navController: NavController){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.16f)
            .background(Color.LightGray),
        horizontalArrangement = Arrangement.SpaceEvenly, // o SpaceBetween, Center, ecc.
        verticalAlignment = Alignment.CenterVertically
    ){

            Button(
                onClick = { navController.navigate(NavigatorScreen.Leaderboard.route) },
                modifier = Modifier.size(100.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "leaderboard")
            }

            Button(
                onClick = { navController.navigate(NavigatorScreen.Game.route) },
                modifier = Modifier.size(100.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "game")
            }

            Button(
                onClick = { navController.navigate(NavigatorScreen.Settings.route) },
                modifier = Modifier.size(100.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Settings")
            }
    }
}



@SuppressLint("ContextCastToActivity")
@Composable
fun GameTopper(navController: NavController){
    val activity = (LocalContext.current as? Activity)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.20f)
            .background(Color.LightGray),
        horizontalArrangement = Arrangement.SpaceEvenly, // o SpaceBetween, Center, ecc.
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(id = R.drawable.title),
            contentDescription = "Descrizione immagine",
            modifier = Modifier
                .weight(23f)
                .fillMaxHeight(),
            contentScale = ContentScale.Fit
        )

        Column (

            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Button(
                onClick = { navController.navigate(NavigatorScreen.HowToPlay.route) },
                modifier = Modifier.size(37.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow)
            ) {
                Text(text = "?")
            }

            Button(
                onClick = { activity?.finish() },
                modifier = Modifier.size(37.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {

            }
        }
    }
}
@Composable
fun SimonColorButton(move: SimonMove, highlighted: Boolean, onClick: () -> Unit){
    val color = when(move){
        SimonMove.RED -> if(highlighted) Color.Red else Color(0xff990000)
        SimonMove.GREEN -> if(highlighted) Color.Green else Color(0xff009900)
        SimonMove.BLUE -> if(highlighted) Color(0xff3333ff) else Color(0xff000099)
        SimonMove.YELLOW -> if(highlighted) Color(0xffffff99) else Color(0xffb3b300)

    }
    Box(
        modifier = Modifier
            .size(180.dp)
            .padding(8.dp)
            .background(color, shape = RoundedCornerShape(16.dp))
            .clickable{ onClick()}
    ){
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