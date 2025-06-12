package com.boxbox.simon.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boxbox.simon.navigator.NavigatorScreen
import com.boxbox.simon.ui.theme.ThemeManager
import com.boxbox.simon.utils.playSound

@Composable
fun PreGameInterface(navController : NavController){
    val context = LocalContext.current
    val theme = ThemeManager.currentTheme

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(start = 15.dp, end = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){

        //image title
        Image(
            painter = painterResource(id = theme.title),
            contentDescription = "",
        )

        Spacer(modifier = Modifier.height(15.dp))

        //button to start the game
        Button(
            onClick = { navController.navigate(NavigatorScreen.Game.route)
                playSound(theme.introSound,context)},
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(8.dp),
        ) {

            Image(
                painter = painterResource(theme.play),
                contentDescription = "",
                modifier = Modifier
                    .size(130.dp),
                contentScale = ContentScale.FillWidth
            )

        }
    }

}