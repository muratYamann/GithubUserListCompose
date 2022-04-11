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
            .padding(start = dimensionResource(id = R.dimen.user_detail_screen_column_padding_start))
    ) {

        Icon(
            modifier = Modifier
                .align(Alignment.End)
                .size(dimensionResource(id = R.dimen.user_detail_screen_close_icon_size))
                .padding(
                    top = dimensionResource(id = R.dimen.user_detail_screen_close_icon_padding_top),
                    end = dimensionResource(id = R.dimen.user_detail_screen_close_icon_padding_end)
                )
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
                .padding(top = dimensionResource(id = R.dimen.user_detail_screen_content_column_padding_top)),
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
                        .padding(dimensionResource(id = R.dimen.user_detail_screen_profile_image_padding))
                        .size(dimensionResource(id = R.dimen.user_detail_screen_profile_image_size))
                        .border(
                            width = dimensionResource(id = R.dimen.user_detail_screen_profile_image_border_width),
                            color = Color.White,
                            CircleShape
                        )
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

            Column(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.user_detail_screen_user_name_column_padding_start))) {
                Text(
                    text = userItem.name ?: "",
                    color = colorResource(id = R.color.white),
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = R.dimen.user_detail_screen_user_name_text_size).value.sp
                )
                Text(
                    text = userItem.username,
                    color = colorResource(id = R.color.github_back_text_color)
                )
            }
        }
        Text(
            modifier = Modifier
                .padding(
                    start = dimensionResource(id = R.dimen.user_detail_screen_bio_padding_start),
                    top = dimensionResource(id = R.dimen.user_detail_screen_bio_padding_top)
                ),
            text = userItem.bio ?: stringResource(R.string.user_detail_empty_bio_text),
            color = colorResource(id = R.color.white)
        )

        Row(
            modifier = Modifier
                .wrapContentHeight()
                .padding(top = dimensionResource(id = R.dimen.user_detail_screen_row_info_padding_top)),
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = dimensionResource(id = R.dimen.user_detail_screen_location_icon_padding_end))
                    .size(dimensionResource(id = R.dimen.ic_back_size)),
                painter = painterResource(id = R.drawable.ic_location),
                contentDescription = null,
                tint = colorResource(id = R.color.github_back_text_color)
            )
            Text(
                text = userItem.location
                    ?: stringResource(R.string.user_detail_screen_temp_location_text),
                color = colorResource(id = R.color.white)
            )
        }


        Row(
            modifier = Modifier
                .wrapContentHeight()
                .padding(top = dimensionResource(id = R.dimen.user_detail_screen_row_follower_padding_top)),
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = dimensionResource(id = R.dimen.user_detail_screen_icon_follower_padding_end))
                    .size(dimensionResource(id = R.dimen.ic_back_size)),
                painter = painterResource(id = R.drawable.ic_people),
                contentDescription = null,
                tint = colorResource(id = R.color.github_back_text_color)
            )

            Text(
                text = "${userItem.followers ?: 0}  ${stringResource(id = R.string.user_detail_screen_follower_text)} ",
                color = colorResource(id = R.color.white)
            )
            Text(
                text = "${userItem.following ?: 0} ${stringResource(id = R.string.user_detail_screen_following_text)} ",
                color = colorResource(id = R.color.white)
            )
        }

        Row(
            modifier = Modifier
                .wrapContentHeight()
                .padding(top = dimensionResource(id = R.dimen.user_detail_screen_repository_rop_padding_top)),
        ) {
            Text(
                text = stringResource(R.string.user_detail_screen_repository_text),
                color = colorResource(id = R.color.white)
            )

            Surface(
                color = colorResource(id = R.color.github_back_text_color),
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.user_detail_screen_surface_round_corner_shape)),
                elevation = dimensionResource(id = R.dimen.user_detail_screen_surface_elevation)
            ) {
                Text(
                    modifier = Modifier.padding(
                        vertical = dimensionResource(id = R.dimen.user_detail_screen_repository_text_vertical),
                        horizontal = dimensionResource(id = R.dimen.user_detail_screen_repository_text_horizontal)
                    ),
                    text = userItem.publicRepos.toString(),
                    fontSize = dimensionResource(id = R.dimen.user_detail_screen_repository_text_size).value.sp,
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

