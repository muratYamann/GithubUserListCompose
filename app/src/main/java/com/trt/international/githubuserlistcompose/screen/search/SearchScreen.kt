package com.trt.international.githubuserlistcompose.screen.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.trt.international.core.DataMapper
import com.trt.international.core.model.UserSearchItem
import com.trt.international.githubuserlistcompose.R
import com.trt.international.githubuserlistcompose.customviews.*
import com.trt.international.githubuserlistcompose.navigate.Routes
import com.trt.international.githubuserlistcompose.screen.search.viewmodel.SearchViewModel

@Composable
fun SearchScreen(navController: NavController, searchViewModel: SearchViewModel = hiltViewModel()) {

    val searchedText = remember { mutableStateOf(String()) }
    val discoverUserIsVisible = remember { mutableStateOf(true) }


    Scaffold(
        backgroundColor = Color.Transparent,
        topBar = {
            CustomSearchBar(
                modifier = Modifier
                    .padding(
                        vertical = 24.dp,
                        horizontal = dimensionResource(id = R.dimen.search_screen_search_bar_horizontal_padding)
                    )
                    .fillMaxWidth(),
                placeholderText = stringResource(id = R.string.search_screen_search_bar_placeholder_text),
                searchText = {
                    if (it.length > 1) {
                        searchedText.value = it
                    } else {
                        searchedText.value = ""
                    }
                },
                onDone = {
                    searchViewModel.getUserFromApi(it)
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Routes.FavoriteScreen.routes)
                }
            ) {
                Icon(Icons.Filled.Favorite, "")
            }
        },
        content = {

            LaunchedEffect(key1 = Unit, block = {
                searchViewModel.getDiscoverUserFromApi()
            })
            DefaultContent(navController, searchViewModel)

            when (searchedText.value) {
                "" -> {
                    searchViewModel.getDiscoverUserFromApi()
                    discoverUserIsVisible.value = true
                    DefaultContent(navController, searchViewModel)
                }

                else -> {
                    discoverUserIsVisible.value = false
                }
            }
        }
    )
}

@Composable
fun DefaultContent(navController: NavController, searchViewModel: SearchViewModel) {
    val userList = remember { mutableStateListOf<List<UserSearchItem>>() }


    searchViewModel.isLoading.observeAsState().value?.let {
        CircularProgressBar(isDisplayed = it)
    }

    searchViewModel.error.observeAsState().value?.let {
        if (it.isNotEmpty()) {
            CircularProgressBar(isDisplayed = false)
        }
    }

    val searchResult = searchViewModel.resultUserApi.observeAsState()
    searchResult.value?.let { it ->
        CircularProgressBar(isDisplayed = false)
        UserResultRowCard(navController, searchViewModel, it)
    }

    // userList.swapList(getDailyItemList()) // Returns a List<DailyItem> with latest values and uses mutable list internally

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserResultRowCard(
    navController: NavController,
    searchViewModel: SearchViewModel,
    userList: List<UserSearchItem>
) {

    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .clickable(enabled = true) {
            navController.navigate(Routes.UserDetailScreen.routes)
        })

    {
        items(count = userList.size, itemContent = { itemIndex ->
            Card(
                backgroundColor = colorResource(id = R.color.github_back_color),
                elevation = 8.dp,
                modifier = Modifier
                    .animateItemPlacement()
                    .height(dimensionResource(id = R.dimen.search_screen_card_height))
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Row {
                        CustomImageViewFromURL(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(60.dp)
                                .border(
                                    width = 1.dp,
                                    color = colorResource(id = R.color.github_back_text_color),
                                    CircleShape
                                )
                                .align(Alignment.CenterVertically)
                                .clip(CircleShape),
                            image = userList[itemIndex].avatarUrl!!,
                        )

                        Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = userList[itemIndex].login!!,
                            color = colorResource(id = R.color.user_list_text_color)
                        )
                    }

                    val (isChecked, setChecked) = remember { mutableStateOf(false) }
                    FavoriteButton(
                        isChecked = isChecked,
                        onClick = {
                            setChecked(!isChecked)
                            setFavoriteUser(searchViewModel, isChecked, userList[itemIndex])
                        }
                    )

                }
            }
        })

    }
}

private fun setFavoriteUser(
    searchViewModel: SearchViewModel,
    isChecked: Boolean,
    userSearchItem: UserSearchItem
) {
    if (isChecked) {
        val userFavorite = userSearchItem.let {
            DataMapper.mapUserSearchItemToUserFavorite(it)
        }
        userFavorite.let {
            searchViewModel.deleteUserFromDb(it)
        }

    } else {
        val userFavorite = userSearchItem.let {
            DataMapper.mapUserSearchItemToUserFavorite(it)
        }
        userFavorite.let {
            searchViewModel.addUserToFavDB(it)
        }

    }
}

fun <T> SnapshotStateList<T>.swapList(newList: List<T>) {
    clear()
    addAll(newList)
}

@Composable
fun SearchFailedItem() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = dimensionResource(id = R.dimen.search_screen_failed_text_horizontal_padding),
                end = dimensionResource(id = R.dimen.search_screen_failed_text_horizontal_padding),
                bottom = dimensionResource(id = R.dimen.search_screen_failed_text_bottom_padding)
            ),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CustomImageViewFromResource(
            modifier = Modifier.size(dimensionResource(id = R.dimen.search_icon_size)),
            image = R.drawable.icons_search
        )

        Text(
            text = stringResource(id = R.string.search_failed_text),
            textAlign = TextAlign.Center,
            color = colorResource(id = R.color.white),
            fontSize = dimensionResource(id = R.dimen.search_screen_failed_text_size).value.sp
        )
    }
}

