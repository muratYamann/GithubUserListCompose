package com.trt.international.githubuserlistcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.trt.international.githubuserlistcompose.navigate.Routes
import com.trt.international.githubuserlistcompose.screen.detail.UserDetailScreen
import com.trt.international.githubuserlistcompose.screen.favorite.FavoriteScreen
import com.trt.international.githubuserlistcompose.screen.search.SearchScreen
import com.trt.international.githubuserlistcompose.ui.theme.GithubUserListComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GithubUserListComposeTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Routes.SearchScreen.routes
                ) {

                    composable(route = Routes.SearchScreen.routes) {
                        SearchScreen(navController)
                    }

                    composable(
                        route = Routes.UserDetailScreen.routes
                    ) {
                        UserDetailScreen(navController)
                    }
                    composable(
                        route = Routes.FavoriteScreen.routes
                    ) {
                        FavoriteScreen(navController)
                    }

                }

            }
        }
    }
}

