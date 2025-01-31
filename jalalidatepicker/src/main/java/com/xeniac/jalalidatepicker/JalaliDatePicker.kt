package com.xeniac.jalalidatepicker

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun JalaliDatePicker(
    modifier: Modifier = Modifier
) {
    Text(
        text = "JalaliDatePicker",
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize()
    )
}