package com.trt.international.githubuserlistcompose.screen.detail

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.trt.international.core.model.UserDetail
import com.trt.international.githubuserlistcompose.R
import com.trt.international.githubuserlistcompose.customviews.CircularProgressBar
import com.trt.international.githubuserlistcompose.customviews.CustomImageViewFromResource
import com.trt.international.githubuserlistcompose.customviews.CustomImageViewFromURL

@Composable
fun UserDetailScreen(
    navController: NavController,
    userDetailViewModel: DetailViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = Unit, block = {
        userDetailViewModel.getUserDetailFromApi("muratYamann")
    })
    UserDetailContent(navController, userDetailViewModel)

}

@Composable
fun UserDetailContent(navController: NavController, searchViewModel: DetailViewModel) {

    searchViewModel.state.observeAsState().value?.let {
        CircularProgressBar(isDisplayed = it)
    }

    searchViewModel.error.observeAsState().value?.let {
        if (it.isNotEmpty()) {
            CircularProgressBar(isDisplayed = false)
        }
    }

    val searchResult = searchViewModel.resultUserApi.observeAsState()
    searchResult.value?.let { it ->
        UserResultRowCard(navController, it)
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
fun UserResultRowCard(navController: NavController, userItem: UserDetail) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp)
    ) {

        Icon(
            modifier = Modifier
                .align(Alignment.End)
                .size(60.dp)
                .padding(top = 20.dp, end = 16.dp),
            painter = painterResource(id = R.drawable.icons_close),
            contentDescription = null,
            tint = colorResource(id = R.color.github_back_text_color)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 46.dp),
            horizontalArrangement = Arrangement.Center,
        ) {

            CustomImageViewFromURL(
                modifier = Modifier
                    .clickable(enabled = true) {

                    }
                    .size(90.dp)
                    .border(width = 2.dp, color = Color.White, CircleShape)
                    .clip(CircleShape),
                image = userItem.avatarUrl!!,
            )

            Column(modifier = Modifier.padding(top = 8.dp, start = 8.dp)) {
                Text(
                    text = userItem.name!!,
                    color = colorResource(id = R.color.white),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = userItem.username,
                    color = colorResource(id = R.color.github_back_text_color)
                )
            }
        }
        Text(
            modifier = Modifier
                .padding(top = 16.dp),
            text = userItem.bio ?: "Bio",
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

@Composable
fun AppDialog(
    modifier: Modifier = Modifier,
    avatarUrl: String,
    dialogState: Boolean = false,
    onDialogPositiveButtonClicked: (() -> Unit)? = null,
    onDialogStateChange: ((Boolean) -> Unit)? = null,
    onDismissRequest: (() -> Unit)? = null,
) {
    val textPaddingAll = 24.dp
    val buttonPaddingAll = 8.dp
    val dialogShape = RoundedCornerShape(16.dp)

    if (dialogState) {
        AlertDialog(
            onDismissRequest = {
                onDialogStateChange?.invoke(false)
                onDismissRequest?.invoke()
            },
            title = null,
            text = null,
            buttons = {

                Column {
                    CustomImageViewFromURL(
                        modifier = Modifier.fillMaxWidth(),
                        avatarUrl
                    )
                    Divider(color = MaterialTheme.colors.onSurface, thickness = 1.dp)

                    Row(
                        modifier = Modifier.padding(all = buttonPaddingAll),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                onDialogStateChange?.invoke(false)
                                onDialogPositiveButtonClicked?.invoke()
                            }
                        ) {
                            Text(
                                text = stringResource(R.string.dialog_ok),
                                color = MaterialTheme.colors.onSurface
                            )
                        }
                    }
                }

            },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false),
            modifier = modifier,
            shape = dialogShape
        )
    }
}
