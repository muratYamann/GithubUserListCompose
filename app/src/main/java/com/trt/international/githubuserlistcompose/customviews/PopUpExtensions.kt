package com.trt.international.githubuserlistcompose.customviews

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun CustomToast(
    message: String,
    isLong: Boolean? = false
) = createToast(LocalContext.current, message, isLong)

private fun createToast(
    context: Context,
    message: String,
    isLong: Boolean? = false
) {
    if (isLong == true) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    } else {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}