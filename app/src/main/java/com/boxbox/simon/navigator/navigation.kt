package com.boxbox.simon.navigator

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.boxbox.simon.viewmodel.SimonViewModel
import com.boxbox.simon.views.creditsInterface
import com.boxbox.simon.views.GameScreen
import com.boxbox.simon.views.howToPlayInterface
import com.boxbox.simon.views.leaderboardInterface
import com.boxbox.simon.views.preGameInterface
import com.boxbox.simon.views.settingInterface

enum class NavigatorScreen(val route: String) {
    Game("game"),
    Settings("settings"),
    Leaderboard("leaderboard"),
    HowToPlay("howToPlay"),
    PreGame("preGame"),
    Credits("credits")
}

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
        composable(NavigatorScreen.Game.route){
            GameScreen(viewModel)
        }

        composable(NavigatorScreen.Leaderboard.route){
            viewModel.setIdle()
            leaderboardInterface()
        }

        composable(NavigatorScreen.Settings.route){
            viewModel.setIdle()
            settingInterface(navController)
        }

        composable(NavigatorScreen.HowToPlay.route){
            viewModel.setIdle()
            howToPlayInterface()
        }

        composable(NavigatorScreen.Credits.route){
            viewModel.setIdle()
            creditsInterface()
        }

        composable(NavigatorScreen.PreGame.route){
            preGameInterface(navController)
        }

    }

}
