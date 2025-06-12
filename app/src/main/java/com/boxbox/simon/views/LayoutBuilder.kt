package com.boxbox.simon.views

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.boxbox.simon.navigator.Nav
import com.boxbox.simon.viewmodel.SimonViewModel

@Composable
fun Screen() {

    // create a navigation controller and the ViewModel
    val navController = rememberNavController()
    val viewModel: SimonViewModel = viewModel()

    // observe the current navigation route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // detect screen orientation (landscape or vertical)
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // choose layout based on orientation
    if (isLandscape) LandscapeLayout(currentRoute, navController, viewModel)
    else VerticalLayout(currentRoute, navController, viewModel)
}


@Composable
fun LandscapeLayout(currentRoute: String?, navController: NavHostController, viewModel: SimonViewModel) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars) //Avoids overlap with the bar
    ) {

        // left side bar (game topper), IF used for a cleaner pre game screen without bars
        Box(Modifier.weight(0.15f).fillMaxHeight()) {
            if (currentRoute != "preGame") GameTopperLandscape(navController)
        }

        // central Box that displays different content based on the current navigation route
        Box(Modifier.weight(0.7f).fillMaxSize()) {
            Nav(
                navController = navController,
                modifier = Modifier,
                viewModel = viewModel
            )
        }

        //right side bar (game footer)
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

        //top Bar (game topper)
        if (currentRoute != "preGame") GameTopper(navController)

        // central Box that displays different content based on the current navigation route
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

        //Bottom Bar (game footer)
        if (currentRoute != "preGame") ResponsiveGameFooter(navController)
    }
}


