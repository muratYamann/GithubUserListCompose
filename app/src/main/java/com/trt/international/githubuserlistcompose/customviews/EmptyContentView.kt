package com.trt.international.githubuserlistcompose.customviews

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.trt.international.githubuserlistcompose.R

@Composable
fun EmptyContentView(
    navController: NavController,
    buttonText: String,
    messageText: String,
    showActionButton: Boolean = true,
    image: Int? = null

) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        image?.let {
            CustomImageViewFromResource(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .size(dimensionResource(id = R.dimen.empty_view_icon_size))
                    .align(Alignment.CenterHorizontally),
                image = it
            )
        }

        Text(
            text = messageText,
            textAlign = TextAlign.Center,
            color = colorResource(id = R.color.white),
            fontSize = dimensionResource(id = R.dimen.search_screen_failed_text_size).value.sp
        )

        Spacer(modifier = Modifier.padding(bottom = 8.dp))

        if (showActionButton) {
            Button(onClick = { navController.navigateUp() }) {
                Text(text = buttonText)
            }
        }

    }
}