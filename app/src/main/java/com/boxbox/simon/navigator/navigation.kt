package com.boxbox.simon.navigator

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.boxbox.simon.viewmodel.SimonViewModel
import com.boxbox.simon.views.CreditsInterface
import com.boxbox.simon.views.GameScreen
import com.boxbox.simon.views.HowToPlayInterface
import com.boxbox.simon.views.LeaderboardInterface
import com.boxbox.simon.views.PreGameInterface
import com.boxbox.simon.views.SettingInterface

// enum class defining the available navigation routes in the app.
enum class NavigatorScreen(val route: String) {
    Game("game"),
    Settings("settings"),
    Leaderboard("leaderboard"),
    HowToPlay("howToPlay"),
    PreGame("preGame"),
    Credits("credits")
}

// Composable function that sets up the navigation graph of the app
@Composable
fun Nav(
    navController: NavHostController,
    modifier: Modifier,
    viewModel: SimonViewModel
){
    NavHost(
        navController = navController,
        startDestination = NavigatorScreen.PreGame.route,
        modifier = modifier
    ){

        // game screen route
        composable(NavigatorScreen.Game.route){
            GameScreen(viewModel)
        }

        // leaderboard screen route
        composable(NavigatorScreen.Leaderboard.route){
            viewModel.setIdle()
            LeaderboardInterface()
        }

        // settings screen route
        composable(NavigatorScreen.Settings.route){
            viewModel.setIdle()
            SettingInterface(navController)
        }

        // how to play screen route
        composable(NavigatorScreen.HowToPlay.route){
            viewModel.setIdle()
            HowToPlayInterface()
        }

        // credits screen route
        composable(NavigatorScreen.Credits.route){
            viewModel.setIdle()
            CreditsInterface()
        }

        // pre game screen route
        composable(NavigatorScreen.PreGame.route){
            PreGameInterface(navController)
        }

    }

}
