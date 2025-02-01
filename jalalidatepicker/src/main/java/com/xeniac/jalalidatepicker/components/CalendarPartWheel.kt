package com.xeniac.jalalidatepicker.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

@Composable
internal fun CalendarPartWheel(
    selectedValue: Int,
    range: Iterable<Int>,
    dividersHeight: Dp,
    dividersColor: Color,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    label: (Int) -> String = { it.toString() },
    onSelectedValueChange: (Int) -> Unit
) {
    ItemPickerWheel(
        selectedValue = selectedValue,
        range = range.toList(),
        dividersHeight = dividersHeight,
        dividersColor = dividersColor,
        textStyle = textStyle,
        label = label,
        onSelectedValueChange = onSelectedValueChange,
        modifier = modifier
    )
}