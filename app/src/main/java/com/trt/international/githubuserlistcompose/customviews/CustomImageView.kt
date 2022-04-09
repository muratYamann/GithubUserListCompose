package com.trt.international.githubuserlistcompose.customviews

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage

@Composable
fun CustomImageViewFromURL(modifier: Modifier, image: String) {
    AsyncImage(
        modifier = modifier,
        model = image,
        contentDescription = null
    )
}

@Composable
fun CustomImageViewFromResource(modifier: Modifier, image: Int) {
    Image(
        modifier = modifier,
        painter = painterResource(id = image),
        contentDescription = null
    )
}