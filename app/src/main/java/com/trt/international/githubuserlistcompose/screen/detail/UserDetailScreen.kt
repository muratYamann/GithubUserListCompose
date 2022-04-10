package com.trt.international.githubuserlistcompose.screen.detail

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.trt.international.core.DataMapper
import com.trt.international.core.model.UserDetail
import com.trt.international.githubuserlistcompose.R
import com.trt.international.githubuserlistcompose.customviews.*
import com.trt.international.githubuserlistcompose.screen.detail.viewmodel.DetailViewModel

@Composable
fun UserDetailScreen(
    itemId: String,
    navController: NavController,
    userDetailViewModel: DetailViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = Unit, block = {
        userDetailViewModel.getUserDetailFromApi(itemId)
        userDetailViewModel.getUserDetailFromDB(itemId)
    })
    UserDetailContent(navController, userDetailViewModel, itemId)

}

@Composable
fun UserDetailContent(
    navController: NavController,
    userDetailViewModel: DetailViewModel,
    itemId: String
) {

    userDetailViewModel.state.observeAsState().value?.let {
       CircularProgressBar(isDisplayed = it)
    }

    userDetailViewModel.error.observeAsState().value?.let {
        if (it.isNotEmpty()) {
         CircularProgressBar(isDisplayed = false)
        }
    }

    val isHasOnFavorite = remember { mutableStateOf(false) }

    val searchResult = userDetailViewModel.resultUserApi.observeAsState()
    val searchSearchFromFavoriteDbResult = userDetailViewModel.resultUserDB.observeAsState()

    searchSearchFromFavoriteDbResult.value?.let { list ->
        list.any { it.name == itemId }
        isHasOnFavorite.value = true
    }

    searchResult.value?.let { userDetail ->
        UserResultRowCard(navController, userDetailViewModel, userDetail, isHasOnFavorite)
    } ?: run {
        SearchFailedItem()
    }

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

@Composable
fun UserResultRowCard(
    navController: NavController,
    detailViewModel: DetailViewModel,
    userItem: UserDetail,
    isHasOnFavorite: MutableState<Boolean>
) {
    val isClickedProfileIcon = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp)
    ) {

        Icon(
            modifier = Modifier
                .align(Alignment.End)
                .size(50.dp)
                .padding(top = 20.dp, end = 16.dp)
                .clickable(enabled = true) {
                    navController.navigateUp()
                },
            painter = painterResource(id = R.drawable.icons_close),
            contentDescription = null,
            tint = colorResource(id = R.color.github_back_text_color)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 46.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                contentAlignment = Alignment.BottomEnd
            ) {

               CustomImageViewFromURL(
                    modifier = Modifier
                        .clickable(enabled = true) {
                            isClickedProfileIcon.value = true
                        }
                        .padding(17.dp)
                        .size(120.dp)
                        .border(width = 2.dp, color = Color.White, CircleShape)
                        .clip(CircleShape),
                    image = userItem.avatarUrl!!,
                )

                if (isClickedProfileIcon.value) {
                    Dialog(onDismissRequest = { isClickedProfileIcon.value = false }) {
                       CustomDialogUI(
                            openDialogCustom = isClickedProfileIcon,
                            image = userItem.avatarUrl!!
                        )
                    }

                }

                val (isChecked, setChecked) = remember { mutableStateOf(false) }

                FavoriteButton(
                    isChecked = isChecked,
                    onClick = {
                        setChecked(!isChecked)
                        setFavoriteUser(detailViewModel, isChecked, userItem)
                    }
                )
            }

            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(
                    text = userItem.name ?: "",
                    color = colorResource(id = R.color.white),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
                Text(
                    text = userItem.username,
                    color = colorResource(id = R.color.github_back_text_color)
                )
            }
        }
        Text(
            modifier = Modifier
                .padding(start = 4.dp, top = 24.dp),
            text = userItem.bio ?: "Empty Bio",
            color = colorResource(id = R.color.white)
        )

        Row(
            modifier = Modifier
                .wrapContentHeight()
                .padding(top = 8.dp),
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 4.dp)
                    .size(dimensionResource(id = R.dimen.ic_back_size)),
                painter = painterResource(id = R.drawable.ic_location),
                contentDescription = null,
                tint = colorResource(id = R.color.github_back_text_color)
            )
            Text(
                text = userItem.location ?: "Temp Location",
                color = colorResource(id = R.color.white)
            )
        }


        Row(
            modifier = Modifier
                .wrapContentHeight()
                .padding(top = 8.dp),
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 4.dp)
                    .size(dimensionResource(id = R.dimen.ic_back_size)),
                painter = painterResource(id = R.drawable.ic_people),
                contentDescription = null,
                tint = colorResource(id = R.color.github_back_text_color)
            )
            Text(
                text = "${userItem.followers ?: 0} followers - ",
                color = colorResource(id = R.color.white)
            )
            Text(
                text = "${userItem.following ?: 0} following",
                color = colorResource(id = R.color.white)
            )
        }

        Row(
            modifier = Modifier
                .wrapContentHeight()
                .padding(top = 8.dp),
        ) {
            Text(
                text = "Repositories ",
                color = colorResource(id = R.color.white)
            )

            Surface(
                color = colorResource(id = R.color.github_back_text_color),
                shape = RoundedCornerShape(16.dp),
                elevation = 2.dp
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 2.dp, horizontal = 4.dp),
                    text = userItem.publicRepos.toString(),
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.white)
                )
            }

        }

    }
}

private fun setFavoriteUser(
    searchViewModel: DetailViewModel,
    isChecked: Boolean,
    userSearchItem: UserDetail
) {
    if (isChecked) {
        val userFavorite = userSearchItem.let {
            DataMapper.mapUserDetailToUserFavorite(it)
        }
        userFavorite.let {
            searchViewModel.deleteUserFromDb(it)
        }

    } else {
        val userFavorite = userSearchItem.let {
            DataMapper.mapUserDetailToUserFavorite(it)
        }
        userFavorite.let {
            searchViewModel.addUserToFavDB(it)
        }

    }
}

