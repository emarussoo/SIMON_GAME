package com.boxbox.simon.navigator

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.boxbox.simon.GameScreen
import com.boxbox.simon.howToPlayInterface
import com.boxbox.simon.leaderboardInterface
import com.boxbox.simon.preGameInterface
import com.boxbox.simon.settingInterface
import com.boxbox.simon.viewmodel.SimonViewModel

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
            GameScreen(viewModel, modifier,navController)
        }

        composable(NavigatorScreen.Leaderboard.route){
            leaderboardInterface()
        }

        composable(NavigatorScreen.Settings.route){
            settingInterface()
        }

        composable(NavigatorScreen.HowToPlay.route){
            howToPlayInterface()
        }

        composable(NavigatorScreen.PreGame.route){
            preGameInterface(navController)
        }



    }

}

enum class NavigatorScreen(val route: String) {
    Game("game"),
    Settings("settings"),
    Leaderboard("leaderboard"),
    HowToPlay("howToPlay"),
    PreGame("preGame"),
    PostGame("postGame")
}