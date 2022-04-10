package com.trt.international.githubuserlistcompose.customviews

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trt.international.githubuserlistcompose.R

@Composable
fun CustomAppBar(
    topBarTitle: String,
    navigationIconClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.topBar_height))
            .background(color = colorResource(id = R.color.github_bar_color)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = dimensionResource(id = R.dimen.all_pages_padding))
                .clickable {
                    navigationIconClick()
                },
            verticalAlignment = Alignment.CenterVertically

        ) {

            CustomImageViewFromResource(
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.ic_back_size))
                    .align(Alignment.CenterVertically),
                image = R.drawable.ic_back
            )

            Text(
                modifier = Modifier
                    .padding(start = dimensionResource(id = R.dimen.topBar_action_button_padding_horizontal)),
                text = topBarTitle,
                color = Color.White,
                fontSize = dimensionResource(id = R.dimen.topBar_title_text_size).value.sp,
                fontWeight = FontWeight.Bold
            )
        }

        CustomImageViewFromResource(
            modifier = Modifier
                .padding(end = 16.dp)
                .size(dimensionResource(id = R.dimen.app_bar_icon_size))
                .align(Alignment.CenterVertically),
            image = R.drawable.app_icon
        )
    }
}