package com.boxbox.simon.views

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.boxbox.simon.navigator.NavigatorScreen
import com.boxbox.simon.ui.theme.ThemeManager

@SuppressLint("ContextCastToActivity", "SuspiciousIndentation")
@Composable
fun GameTopper(navController: NavController) {

    val activity = (LocalContext.current as? Activity)
    var showExitPopUp = remember { mutableStateOf(false) }
    if (showExitPopUp.value) ExitPopUp(showPopUp = showExitPopUp, activity)
    val theme = ThemeManager.currentTheme

    Row(modifier = Modifier
        .padding(top = 20.dp)
        .fillMaxHeight(0.15f)
        .background(color = Color.Transparent)
        .padding( start = 15.dp, end = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(15.dp))
    {

        Image(
            painter = painterResource(id = theme.title),
            contentDescription = "",
            modifier = Modifier
                .weight(1f)
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
                painter = painterResource(id = theme.help),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .clickable { navController.navigate(NavigatorScreen.HowToPlay.route) }
            )

            Image(
                painter = painterResource(id = theme.quit),
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
    val theme = ThemeManager.currentTheme
    var showExitPopUp = remember { mutableStateOf(false) }

    if (showExitPopUp.value){
        ExitPopUp(showPopUp = showExitPopUp, activity)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(start = 25.dp, bottom = 15.dp, top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp))
    {

        Image(
            painter = painterResource(theme.landTitle),
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
                painter = painterResource(id = theme.help),
                contentDescription = "",
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxWidth()
                    .clickable(onClick = { navController.navigate(NavigatorScreen.HowToPlay.route) })

            )

            Image(
                painter = painterResource(id = theme.quit),
                contentDescription = "",
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxWidth()
                    .clickable(onClick = { showExitPopUp.value = true })
            )

        }
    }
}




@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ResponsiveGameFooter(navController: NavController) {
    BoxWithConstraints {

        val theme = ThemeManager.currentTheme
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        // Stato di scala per ogni bottone
        val scaleLeaderboard by animateFloatAsState(
            targetValue = if (currentRoute == NavigatorScreen.Leaderboard.route) 1.3f else 1f,
            animationSpec = tween(durationMillis = 200),
            label = "scaleLeaderboard"
        )

        val scaleGame by animateFloatAsState(
            targetValue = if (currentRoute == NavigatorScreen.Game.route) 1.3f else 1f,
            animationSpec = tween(durationMillis = 200),
            label = "scaleGame"
        )

        val scaleSettings by animateFloatAsState(
            targetValue = if (currentRoute == NavigatorScreen.Settings.route) 1.3f else 1f,
            animationSpec = tween(durationMillis = 200),
            label = "scaleSettings"
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.12f)
                .background(Color.Transparent),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Leaderboard Button
            Button(
                onClick = { navController.navigate(NavigatorScreen.Leaderboard.route) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .graphicsLayer(scaleX = scaleLeaderboard, scaleY = scaleLeaderboard)
            ) {
                Image(
                    painter = painterResource(id = theme.cup),
                    contentDescription = "",
                    modifier = Modifier.scale(0.7f),
                    contentScale = ContentScale.Fit
                )
            }

            // Game Button
            Button(
                onClick = { navController.navigate(NavigatorScreen.Game.route) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .graphicsLayer(scaleX = scaleGame, scaleY = scaleGame)
            ) {
                Image(
                    painter = painterResource(id = theme.joystick),
                    contentDescription = "",
                    modifier = Modifier.scale(0.7f),
                    contentScale = ContentScale.Fit
                )
            }

            // Settings Button
            Button(
                onClick = { navController.navigate(NavigatorScreen.Settings.route) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.graphicsLayer(scaleX = scaleSettings, scaleY = scaleSettings)
            ) {
                Image(
                    painter = painterResource(id = theme.settings),
                    contentDescription = "",
                    modifier = Modifier.scale(0.7f),
                    contentScale = ContentScale.Fit
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

        val theme = ThemeManager.currentTheme
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        //It manages the scale for every app section
        val scaleLeaderboard by animateFloatAsState(
            targetValue = if (currentRoute == NavigatorScreen.Leaderboard.route) 1.3f else 1f,
            animationSpec = tween(durationMillis = 200),
            label = "scaleLeaderboard"
        )

        val scaleGame by animateFloatAsState(
            targetValue = if (currentRoute == NavigatorScreen.Game.route) 1.3f else 1f,
            animationSpec = tween(durationMillis = 200),
            label = "scaleGame"
        )

        val scaleSettings by animateFloatAsState(
            targetValue = if (currentRoute == NavigatorScreen.Settings.route) 1.3f else 1f,
            animationSpec = tween(durationMillis = 200),
            label = "scaleSettings"
        )

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
                    painter = painterResource(id = theme.cup),
                    contentDescription = "",
                    modifier = Modifier.scale(0.7f).graphicsLayer(scaleX = scaleLeaderboard, scaleY = scaleLeaderboard),
                    contentScale = ContentScale.Fit
                )
            }

            Button(
                onClick = { navController.navigate(NavigatorScreen.Game.route) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(8.dp),
            ) {
                Image(
                    painter = painterResource(id = theme.joystick),
                    contentDescription = "",
                    modifier = Modifier.scale(0.7f).graphicsLayer(scaleX = scaleGame, scaleY = scaleGame),
                    contentScale = ContentScale.Fit
                )
            }

            Button(
                onClick = { navController.navigate(NavigatorScreen.Settings.route) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(8.dp),
            ) {
                Image(
                    painter = painterResource(id = theme.settings),
                    contentDescription = "",
                    modifier = Modifier.scale(0.7f).graphicsLayer(scaleX = scaleSettings, scaleY = scaleSettings),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

