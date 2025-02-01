package com.xeniac.jalalidatepicker.utils

import android.content.Context
import android.os.Parcelable
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
internal sealed class UiText : Parcelable {
    data class DynamicString(val value: String) : UiText()

    class StringResource(
        @StringRes val resId: Int,
        vararg val args: @RawValue Any
    ) : UiText()

    class StringArrayResource(
        @ArrayRes val resId: Int,
        val index: Int
    ) : UiText()

    @Composable
    fun asString(): String = when (this) {
        is DynamicString -> value
        is StringResource -> stringResource(id = resId, *args)
        is StringArrayResource -> stringArrayResource(id = resId)[index]
    }

    fun asString(context: Context): String = when (this) {
        is DynamicString -> value
        is StringResource -> context.getString(resId, *args)
        is StringArrayResource -> context.resources.getStringArray(resId)[index]
    }
}