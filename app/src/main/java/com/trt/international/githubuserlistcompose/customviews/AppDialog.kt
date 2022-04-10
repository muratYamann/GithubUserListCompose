package com.trt.international.githubuserlistcompose.customviews

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomDialogUI(
    modifier: Modifier = Modifier,
    openDialogCustom: MutableState<Boolean>,
    image: String
) {

    Column(
        modifier
            .background(Color.Transparent)
            .clip(shape = RoundedCornerShape(16.dp))) {
        CustomImageViewFromURL(
            modifier = Modifier
                .padding(top = 35.dp)
                .fillMaxWidth(),
            image
        )
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            TextButton(onClick = {
                openDialogCustom.value = false
            }) {
                Text(
                    "Ok",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                )
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(name = "Custom Dialog")
@Composable
fun CustomDialogUI() {
    CustomDialogUI(openDialogCustom = mutableStateOf(false), image = "")
}
