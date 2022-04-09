package com.trt.international.githubuserlistcompose.navigate

sealed class Routes(val routes: String) {
    object SearchScreen : Routes(routes = "search_screen")
    object UserDetailScreen : Routes(routes = "user_detail_screen")
    object FavoriteScreen : Routes(routes = "favorite_screen")
}