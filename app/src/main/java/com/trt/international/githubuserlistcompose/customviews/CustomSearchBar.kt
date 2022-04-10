package com.trt.international.githubuserlistcompose.customviews

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.trt.international.githubuserlistcompose.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomSearchBar(
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    placeholderText: String?,
    searchText: (String) -> Unit = { },
    onDone: (String) -> Unit
) {
    val textState = remember { mutableStateOf(String()) }
    val keyboardController = LocalSoftwareKeyboardController.current


    Column(modifier = Modifier.wrapContentHeight()) {
        OutlinedTextField(
            modifier = modifier,
            placeholder = {
                if (placeholderText != null) {
                    Text(
                        text = placeholderText,
                        color = colorResource(id = R.color.search_bar_placeholder_color)
                    )
                }
            },
            value = textState.value,
            onValueChange = {
                textState.value = it
                searchText(textState.value)
            },
            textStyle = TextStyle(fontSize = dimensionResource(id = R.dimen.search_text_size).value.sp),
            trailingIcon = {
                if (textState.value.isNotEmpty()) {
                    Icon(
                        modifier = Modifier.clickable(enabled = true) {
                            textState.value = ""
                            searchText(textState.value)
                        },
                        imageVector = Icons.Default.Clear,
                        contentDescription = "",
                        tint = colorResource(id = R.color.search_bar_placeholder_color)
                    )
                }
            },
            singleLine = true,
            keyboardActions = KeyboardActions {
                onDone(textState.value)
                keyboardController?.hide()
            },

            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = colorResource(id = R.color.bg_textfield),
                textColor = colorResource(id = R.color.white),
                unfocusedLabelColor = Color.White.copy(0.5f),
                focusedLabelColor = Color.White.copy(0.5f),
                focusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.textField_radius)),
        )
    }
}
