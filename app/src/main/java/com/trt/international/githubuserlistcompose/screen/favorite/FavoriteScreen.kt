package com.trt.international.githubuserlistcompose.screen.favorite

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.trt.international.core.model.UserFavorite
import com.trt.international.githubuserlistcompose.R
import com.trt.international.githubuserlistcompose.customviews.*
import com.trt.international.githubuserlistcompose.navigate.Routes
import com.trt.international.githubuserlistcompose.screen.favorite.viewmodel.FavoriteViewModel

@Composable
fun FavoriteScreen(
    navController: NavController,
    favoriteViewModel: FavoriteViewModel = hiltViewModel()
) {

    Scaffold(
        backgroundColor = Color.Transparent,
        topBar = {
            CustomAppBar(
                topBarTitle = stringResource(R.string.favorite_list),
                navigationIconClick = { navController.navigateUp() }
            )
        },
        content = {
            LaunchedEffect(key1 = Unit, block = {
                favoriteViewModel.getFavoriteUserList()
            })

            ObserveFavoriteList(navController, favoriteViewModel)
        }
    )

}

@Composable
fun ObserveFavoriteList(navController: NavController, favoriteViewModel: FavoriteViewModel) {
    favoriteViewModel.isLoading.observeAsState().value?.let {
        CircularProgressBar(isDisplayed = it)
    }

    favoriteViewModel.error.observeAsState().value?.let {
        CircularProgressBar(isDisplayed = false)
        if (it.isNotEmpty()) {
            CustomToast(message = it, isLong = true)
        }
    }

    val favoriteUserList = favoriteViewModel.resultUserApi.observeAsState()
    favoriteUserList.value?.let { it ->
        CircularProgressBar(isDisplayed = false)
        if (it.isNullOrEmpty()) {
            EmptyContentView(
                navController, stringResource(R.string.discover_button_text), stringResource(
                    R.string.favorite_screen_empty_view_message
                )
            )
        } else {
            UserResultRowCard(navController, favoriteViewModel, it)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserResultRowCard(
    navController: NavController,
    favoriteViewModel: FavoriteViewModel,
    userList: List<UserFavorite>
) {

    LazyVerticalGrid(
        cells = GridCells.Fixed(integerResource(id = R.integer.favorite_screen_grid_cell_count)),
        modifier = Modifier
            .fillMaxSize()
    )
    {
        items(count = userList.size, itemContent = { itemIndex ->
            Card(
                backgroundColor = colorResource(id = R.color.github_favorite_card_color),
                elevation = dimensionResource(id = R.dimen.favorite_screen_card_elevation),
                modifier = Modifier
                    .clickable(enabled = true) {
                        navController.navigate(Routes.UserDetailScreen.itemId(userList[itemIndex].username))
                    }
                    .fillMaxWidth()
                    .animateItemPlacement()
                    .wrapContentHeight()
                    .padding(
                        bottom = dimensionResource(id = R.dimen.favorite_screen_card_bottom_padding),
                        top = dimensionResource(id = R.dimen.favorite_screen_card_top_padding),
                        start = dimensionResource(id = R.dimen.favorite_screen_card_start_padding),
                        end = dimensionResource(id = R.dimen.favorite_screen_card_end_padding)
                    )
                    .border(
                        dimensionResource(id = R.dimen.favorite_screen_card_border_with),
                        color = colorResource(id = R.color.github_favorite_card_color),
                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.favorite_screen_card_border_radius))
                    )
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.favorite_screen_card_border_radius))),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {

                    val (isChecked, setChecked) = remember { mutableStateOf(true) }
                    FavoriteButton(
                        isChecked = isChecked,
                        onClick = {
                            setChecked(!isChecked)
                            setFavoriteUser(favoriteViewModel, isChecked, userList[itemIndex])
                        }
                    )

                    CustomImageViewFromURL(
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.favorite_screen_image_size))
                            .border(
                                width = dimensionResource(id = R.dimen.favorite_screen_card_border_with),
                                color = colorResource(id = R.color.github_back_text_color),
                                CircleShape
                            )
                            .align(Alignment.CenterHorizontally)
                            .clip(CircleShape),
                        image = userList[itemIndex].avatarUrl!!,
                    )

                    Text(
                        modifier = Modifier
                            .padding(
                                top = dimensionResource(id = R.dimen.favorite_screen_username_text_padding_top),
                                bottom = dimensionResource(id = R.dimen.favorite_screen_username_text_padding_bottom)
                            )
                            .align(Alignment.CenterHorizontally),
                        text = userList[itemIndex].username,
                        color = colorResource(id = R.color.user_list_text_color)
                    )
                }
            }
        })
    }
}

private fun setFavoriteUser(
    favoriteViewModel: FavoriteViewModel,
    isChecked: Boolean,
    userFavorite: UserFavorite
) {
    if (isChecked) {
        favoriteViewModel.deleteUserFromDb(userFavorite)

    } else {
        favoriteViewModel.addUserToFavDB(userFavorite)
    }
}