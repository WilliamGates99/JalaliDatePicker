package com.xeniac.jalalidatepicker.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * Represents a selected date in the Jalali (Shamsi) calendar.
 *
 * This data class encapsulates the year, month, and day components of a date
 * according to the Jalali calendar system, also known as the Persian or Shamsi calendar.
 *
 * It is designed to be easily serialized and
 * passed between different components of an Android application.
 *
 * @property year The year component of the selected date in the Jalali calendar.
 * @property month The month component of the selected date in the Jalali calendar.
 *   The month is represented as an integer, typically ranging from 1 to 12.
 * @property day The day component of the selected date in the Jalali calendar.
 *   The day is represented as an integer, typically ranging from 1 to 31.
 *
 * @see Serializable
 * @see Parcelize
 * @see Parcelable
 */
@Serializable
@Parcelize
data class SelectedJalaliDate(
    val year: Int,
    val month: Int,
    val day: Int
) : Parcelable