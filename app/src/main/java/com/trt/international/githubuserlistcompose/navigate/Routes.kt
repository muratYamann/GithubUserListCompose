package com.trt.international.githubuserlistcompose.navigate

sealed class Routes(val routes: String) {

    object SearchScreen : Routes(routes = "search_screen")

    object UserDetailScreen : Routes(routes = "user_detail_screen/?itemId={itemId}") {
        fun itemId(itemId: String) = "user_detail_screen/?itemId=$itemId"
    }

    object FavoriteScreen : Routes(routes = "favorite_screen")
}

