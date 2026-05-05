package com.miempresa.jsonplaceapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.miempresa.jsonplaceapp.ui.screen.DetailScreen
import com.miempresa.jsonplaceapp.ui.screen.MainScreen
import com.miempresa.jsonplaceapp.ui.viewmodel.DetailViewModel
import com.miempresa.jsonplaceapp.ui.viewmodel.MainViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            val viewModel: MainViewModel = hiltViewModel()
            MainScreen(
                viewModel = viewModel,
                onPostClick = { postId ->
                    navController.navigate("detail/$postId")
                }
            )
        }

        composable(
            route = "detail/{postId}",
            arguments = listOf(
                navArgument("postId") { type = NavType.IntType }
            )
        ) {
            val viewModel: DetailViewModel = hiltViewModel()
            DetailScreen(viewModel = viewModel)
        }
    }
}