package com.boxbox.simon.views

import android.R.id.bold
import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.boxbox.simon.R
import com.boxbox.simon.navigator.NavigatorScreen
import com.boxbox.simon.ui.theme.ThemeManager

@SuppressLint("ContextCastToActivity", "SuspiciousIndentation")
@Composable
fun GameTopper(navController: NavController) {
    val activity = (LocalContext.current as? Activity)
    var showPopUp by remember { mutableStateOf(false) }

    if (showPopUp){
        AlertDialog(
            onDismissRequest = { showPopUp = false },
            title = { Text(stringResource(R.string.uscita))},
            text = { Text(stringResource(R.string.vuoi_uscire_dal_gioco)) },
            confirmButton = {
                TextButton(onClick = {
                    showPopUp = false
                    activity?.finishAffinity()
                }){
                    Text(stringResource(R.string.s))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showPopUp = false
                }) {
                    Text(stringResource(R.string.no))
                }
            }
        )
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
                    .clickable { showPopUp = true }
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


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ResponsiveGameFooter(navController: NavController) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.16f)
    ) {
        val width = maxWidth
        val imageSize = when {
            width < 360.dp -> 35.dp
            width < 480.dp -> 55.dp
            else -> 75.dp
        }

        val centerX = width / 2

        Box(modifier = Modifier.fillMaxSize()) {
            // Bottone centrale (esattamente al centro orizzontale)
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
            ) {
                FooterButton(
                    imageRes = ThemeManager.currentTheme.joystick,
                    label = "Play",
                    imageSize = imageSize,
                    onClick = { navController.navigate(NavigatorScreen.Game.route) }
                )
            }

            // Bottone sinistra
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 32.dp) // Distanza dal bordo sinistro
            ) {
                FooterButton(
                    imageRes = ThemeManager.currentTheme.cup,
                    label = "Best",
                    imageSize = imageSize,
                    onClick = { navController.navigate(NavigatorScreen.Leaderboard.route) }
                )
            }

            // Bottone destra
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 32.dp) // Distanza dal bordo destro
            ) {
                FooterButton(
                    imageRes = ThemeManager.currentTheme.settings,
                    label = "Settings",
                    imageSize = imageSize,
                    onClick = { navController.navigate(NavigatorScreen.Settings.route) }
                )
            }
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ResponsiveGameFooterLandscape(navController: NavController) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
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
            FooterButton(
                imageRes = ThemeManager.currentTheme.cup,
                label = "Best",
                imageSize = imageSize,
                onClick = { navController.navigate(NavigatorScreen.Leaderboard.route) }
            )

            FooterButton(
                imageRes = ThemeManager.currentTheme.joystick,
                label = "Play",
                imageSize = imageSize,
                onClick = { navController.navigate(NavigatorScreen.Game.route) }
            )

            FooterButton(
                imageRes = ThemeManager.currentTheme.settings,
                label = "Settings",
                imageSize = imageSize,
                onClick = { navController.navigate(NavigatorScreen.Settings.route) }
            )
        }
    }
}

@Composable
fun FooterButton(
    imageRes: Int,
    label: String,
    imageSize: Dp,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .clickable(onClick = onClick).background(color = Color.Transparent),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = label,
            modifier = Modifier
                .size(imageSize)
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = Color.Black,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontWeight = FontWeight.Bold,
        )
    }
}
