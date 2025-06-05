package com.boxbox.simon.views

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boxbox.simon.R
import com.boxbox.simon.navigator.NavigatorScreen
import com.boxbox.simon.ui.theme.ThemeManager

@SuppressLint("ContextCastToActivity", "SuspiciousIndentation")
@Composable
fun GameTopper(navController: NavController) {
    val activity = (LocalContext.current as? Activity)
    var showExitPopUp = remember { mutableStateOf(false) }

    if (showExitPopUp.value){
        ExitPopUp(showPopUp = showExitPopUp, activity)
    }

    Row(modifier = Modifier
        .padding(top = 20.dp)
        .fillMaxHeight(0.15f)
        .background(color = Color.Transparent)
        .padding( start = 15.dp, end = 15.dp),
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

        Column(
            modifier = Modifier
                .weight(0.1f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = ThemeManager.currentTheme.help),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .clickable { navController.navigate(NavigatorScreen.HowToPlay.route) }
            )

            Image(
                painter = painterResource(id = ThemeManager.currentTheme.quit),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showExitPopUp.value = true }
            )
        }
    }
}


@SuppressLint("ContextCastToActivity", "SuspiciousIndentation")
@Composable
fun GameTopperLandscape(navController: NavController) {
    val activity = (LocalContext.current as? Activity)

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(start = 5.dp, bottom = 15.dp, top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp))
    {

        Image(
            painter = painterResource(ThemeManager.currentTheme.landTitle),
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


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ResponsiveGameFooter(navController: NavController){
    BoxWithConstraints (){
        val width = maxWidth
        val imageSize = when {
            width < 360.dp -> 35.dp
            width < 480.dp -> 55.dp
            else -> 75.dp
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.12f)
                .background(Color.Transparent),
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


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ResponsiveGameFooterLandscape(navController: NavController) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val height = maxHeight
        val imageSize = when {
            height < 360.dp -> 35.dp
            height < 480.dp -> 55.dp
            else -> 75.dp
        }

        Column(
            modifier = Modifier.fillMaxSize(),
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

