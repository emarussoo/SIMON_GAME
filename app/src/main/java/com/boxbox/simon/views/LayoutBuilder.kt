package com.boxbox.simon.views

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.boxbox.simon.model.Difficulty
import com.boxbox.simon.navigator.Nav
import com.boxbox.simon.viewmodel.SimonViewModel

@Composable
fun screen() {
    val navController = rememberNavController()
    val viewModel: SimonViewModel = viewModel()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape)
        LandscapeLayout(currentRoute, navController, viewModel)
    else
        VerticalLayout(currentRoute, navController, viewModel)
}

@Composable
fun LandscapeLayout(currentRoute: String?, navController: NavHostController, viewModel: SimonViewModel) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars) //Avoids overlap with the bar
    ) {
        Box(Modifier.weight(0.15f).fillMaxHeight()) {
            if (currentRoute != "preGame") GameTopperLandscape(navController)
        }

        Box(Modifier.weight(0.7f).fillMaxSize()) {
            Nav(
                navController = navController,
                modifier = Modifier,
                viewModel = viewModel
            )
        }

        Box(Modifier.weight(0.15f).fillMaxHeight()) {
            if (currentRoute != "preGame") ResponsiveGameFooterLandscape(navController)
        }
    }
}

@Composable
fun VerticalLayout(
    currentRoute: String?,
    navController: NavHostController,
    viewModel: SimonViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars) //Manages status + nav bar
    ) {
        //Top Bar (only if we're not in the "preGame" screen)
        if (currentRoute != "preGame") {
            GameTopper(navController)
        }

        // Contenuto principale
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Nav(
                navController = navController,
                modifier = Modifier.fillMaxSize(),
                viewModel = viewModel
            )
        }

        //Bottom Bar (only if we're not in the "preGame" screen)
        if (currentRoute != "preGame") {
            ResponsiveGameFooter(navController)
        }
    }
}


