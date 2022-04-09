package com.trt.international.githubuserlistcompose.customviews

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.trt.international.githubuserlistcompose.R

@Composable
fun CustomBackNavigationBar(
    topBarTitle: String,
    navigationIconClick: () -> Unit,
    actionButonText: String? = null,
    actionClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.topBar_height))
            .background(color = Color.Black),
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

        actionButonText?.let {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .clickable {
                        actionClick()
                    }
            ) {
                Text(
                    modifier = Modifier
                        .padding(end = dimensionResource(id = R.dimen.all_pages_padding))
                        .align(Alignment.Center),
                    text = it,
                    color = Color.White,
                    fontSize = dimensionResource(id = R.dimen.topBar_title_text_size).value.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
@Preview
fun TestTRTBackNavigationBar() {
    CustomBackNavigationBar("Edit Profile", {}, "Save", {})
}