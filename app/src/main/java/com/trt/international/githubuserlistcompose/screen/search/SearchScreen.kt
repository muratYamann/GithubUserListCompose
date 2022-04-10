package com.trt.international.githubuserlistcompose.screen.search

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
    val isSearched = remember { mutableStateOf(false) }


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
                    Log.d("callService", "getUserFromApi()")
                    isSearched.value = true
                    searchViewModel.getUserFromApi(it)
                })
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                icon = { Icon(Icons.Filled.Favorite, "") },
                text = { Text("Favorite List") },
                onClick = { navController.navigate(Routes.FavoriteScreen.routes) },
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            )
        },
        content = {
            val resultUserApi = searchViewModel.resultUserApi.observeAsState()
            if (resultUserApi.value.isNullOrEmpty()) {
                LaunchedEffect(key1 = Unit) {
                    Log.d("callService", "getDiscoverUserFromApi()")
                    searchViewModel.getDiscoverUserFromApi()
                }
                DefaultContent(navController, searchViewModel, isSearched.value)
            }

            if (isSearched.value) {
                CircularProgressBar(isDisplayed = false)
                DefaultContent(navController, searchViewModel, isSearched.value)
            }

//            when (searchedText.value) {
//                "" -> {
//                    Log.d("callService","getDiscoverUserFromApi() from searched text")
//                    CircularProgressBar(isDisplayed = false)
//                    searchViewModel.getDiscoverUserFromApi()
//                    DefaultContent(navController, searchViewModel)
//                }
//            }
        }
    )
}

@Composable
fun DefaultContent(
    navController: NavController,
    searchViewModel: SearchViewModel,
    isSearched: Boolean
) {

    searchViewModel.isLoading.observeAsState().value?.let {
        CircularProgressBar(isDisplayed = it)
    }

    searchViewModel.error.observeAsState().value?.let {
        CircularProgressBar(isDisplayed = false)
        if (it.isNotEmpty()) {
            CustomToast(message = it, isLong = true)
            EmptyContentView(
                navController,
                buttonText = "Discover",
                messageText = "Internal error. Please search new user \n\n error message $it ",
                image = R.drawable.icons_search,
                showActionButton = false
            )
        }
    }

    if (isSearched) {
        val searchResult = searchViewModel.resultUserApi.observeAsState()
        searchResult.value?.let { it ->
            CircularProgressBar(isDisplayed = false)
            if (it.isNullOrEmpty()) {
                EmptyContentView(
                    navController,
                    buttonText = "Discover",
                    messageText = "Your searched user list is empty."
                )
            } else {
                UserResultRowCard(navController, searchViewModel, it)
            }
        }
    } else {
        val searchDiscoverUserResult = searchViewModel.resultDiscoverUserApi.observeAsState()
        searchDiscoverUserResult.value?.let { it ->
            CircularProgressBar(isDisplayed = false)
            if (it.isNullOrEmpty()) {
                EmptyContentView(
                    navController,
                    buttonText = "Discover",
                    messageText = "Your searched user list is empty."
                )
            } else {
                UserResultRowCard(navController, searchViewModel, it)
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserResultRowCard(
    navController: NavController,
    searchViewModel: SearchViewModel,
    userList: List<UserSearchItem>
) {

    LazyColumn(modifier = Modifier.fillMaxSize())

    {
        items(count = userList.size, itemContent = { itemIndex ->
            Card(
                backgroundColor = colorResource(id = R.color.github_back_color),
                elevation = 8.dp,
                modifier = Modifier
                    .clickable(enabled = true) {
                        navController.navigate(Routes.UserDetailScreen.itemId(itemId = "${userList[itemIndex].login}"))
                    }
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

