package com.boxbox.simon

import android.graphics.Color.red
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ListItemDefaults.contentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.boxbox.simon.ui.theme.SIMONTheme
import com.boxbox.simon.utils.SimonColor
import com.boxbox.simon.viewmodel.SimonViewModel
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SIMONTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel = SimonViewModel()
                    GameScreen(viewModel, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CounterScreen(viewModel: SimonViewModel, modifier: Modifier){
    val count by viewModel.counter.collectAsState()

    Column(horizontalAlignment = Alignment.CenterHorizontally){
        Text("Contatore: $count")
        /*Button(onClick = {viewModel.increment()}){
            Text("Incrementa")
        }*/
    }

}

@Composable
fun GameScreen(viewModel: SimonViewModel, modifier: Modifier){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        GameHeader(viewModel)
        Spacer(modifier = Modifier.height(30.dp))
        ColorGrid(onColorClick = { viewModel.onColorClicked() })
        Spacer(modifier = Modifier.height(35.dp))
        GameFooter(viewModel)
    }
}

@Composable
fun GameHeader(viewModel: SimonViewModel){
    val count by viewModel.counter.collectAsState()
    Column(horizontalAlignment = Alignment.CenterHorizontally){
        Text("SCORE: $count", fontSize = 24.sp, modifier = Modifier.border(1.dp, Color.Red))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(16.dp)
                .border(1.dp, Color.Red)

        ){
            Text("Icona")
            Text("|||||||||||||||||||||||||||||||", modifier = Modifier.border(1.dp, Color.Red))
        }

    }
}

@Composable
fun ColorGrid(onColorClick: (SimonColor) -> Unit){
    Column(modifier = Modifier
        .border(1.dp, Color.Red)
        .background(Color.Gray)){
        Row{
            SimonColorButton(SimonColor.Red, onColorClick)
            SimonColorButton(SimonColor.Green, onColorClick)

        }
        Row{
            SimonColorButton(SimonColor.Blue, onColorClick)
            SimonColorButton(SimonColor.Yellow, onColorClick)

        }
    }
}


@Composable
fun GameFooter(viewModel: SimonViewModel){
    Row(){
        Text("BEST", modifier = Modifier.size(120.dp, 120.dp).border(1.dp, Color.Green))
        Text("DIFFICULTY", modifier = Modifier.size(120.dp, 120.dp).border(1.dp, Color.Green))
        Column (modifier = Modifier.size(120.dp, 120.dp).border(1.dp, Color.Green), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp) ){
            Text("Settings")
            TextButton(
                onClick = {},
                modifier = Modifier.background(Color.Red)
            ){
                Text("QUIT")
            }
        }

    }
}


@Composable
fun SimonColorButton(color: SimonColor, onClick: (SimonColor)-> Unit){
    Box(
        modifier = Modifier
            .size(180.dp)
            .padding(8.dp)
            .background(color.toColor(), shape = RoundedCornerShape(16.dp))
            .clickable{ onClick(color)}
    ){
    }
}

fun dummy(){

}
