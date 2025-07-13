package com.xeniac.jalalidatepicker

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xeniac.jalalidatepicker.components.CalendarPartWheel
import com.xeniac.jalalidatepicker.handler.DatePickerHandler
import com.xeniac.jalalidatepicker.handler.DatePickerHandlerImpl
import com.xeniac.jalalidatepicker.models.DatePickerDefaults
import com.xeniac.jalalidatepicker.models.SelectedJalaliDate
import com.xeniac.jalalidatepicker.utils.JalaliCalendarUtil
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * A composable date picker for selecting dates in the Jalali (Shamsi) calendar.
 *
 * This composable provides a user interface for selecting a date using the Jalali calendar system.
 * It displays three scrollable wheels for selecting the year, month, and day.
 *
 * @param initialSelectedDate The initial selected date in the Gregorian calendar.
 *   Defaults to the current date and time.
 * @param selectableYearsRange An optional range of years that can be selected.
 * @param isSelectFromFutureEnabled If `false`, the maximum selectable date will be the current date.
 *   If `true`, future dates can be selected.
 *   Defaults to `false`.
 * @param datePickerDefaults Configuration options for the date picker, such as
 *   the number of selectable years and future years. Defaults to [DatePickerDefaults].
 * @param dividersHeight The height of the dividers between the date picker wheels.
 *   Defaults to 2.dp.
 * @param dividersColor The color of the dividers.
 *   Defaults to the primary color from the [MaterialTheme] color scheme.
 * @param textStyle The text style to use for the date picker labels.
 *   Defaults to the current [LocalTextStyle] with a font size of 14.sp.
 * @param showMonthNumber If `true`, the month number will be displayed along with the month name.
 *   Defaults to `false`.
 * @param onSelectedDateChange A callback that is invoked when the selected date changes.
 *   It provides the newly selected date as a [SelectedJalaliDate] object.
 *
 * @see SelectedJalaliDate
 * @see DatePickerDefaults
 * @see MaterialTheme
 * @see LocalTextStyle
 */
@OptIn(ExperimentalTime::class)
@Composable
fun JalaliDatePicker(
    modifier: Modifier = Modifier,
    initialSelectedDate: Instant = Clock.System.now(),
    selectableYearsRange: Iterable<Int>? = null,
    isSelectFromFutureEnabled: Boolean = false,
    datePickerDefaults: DatePickerDefaults = DatePickerDefaults(),
    dividersHeight: Dp = 2.dp,
    dividersColor: Color = MaterialTheme.colorScheme.primary,
    textStyle: TextStyle = LocalTextStyle.current.copy(
        fontSize = 14.sp
    ),
    showMonthNumber: Boolean = false,
    onSelectedDateChange: (SelectedJalaliDate) -> Unit
) {
    val context = LocalContext.current

    val datePickerHandler: DatePickerHandler by lazy {
        DatePickerHandlerImpl(
            selectableYearsRange = selectableYearsRange,
            isSelectFromFutureEnabled = isSelectFromFutureEnabled,
            datePickerDefaults = datePickerDefaults
        )
    }

    val initialSelectedDateJalali = JalaliCalendarUtil.apply {
        convertGregorianToJalali(date = initialSelectedDate)
    }

    LaunchedEffect(key1 = Unit) {
        onSelectedDateChange(
            SelectedJalaliDate(
                year = initialSelectedDateJalali.year,
                month = initialSelectedDateJalali.month,
                day = initialSelectedDateJalali.day
            )
        )
    }

    var selectedYear by remember { mutableIntStateOf(initialSelectedDateJalali.year) }
    var selectedMonth by remember { mutableIntStateOf(initialSelectedDateJalali.month) }
    var selectedDay by remember { mutableIntStateOf(initialSelectedDateJalali.day) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RectangleShape)
    ) {
        CalendarPartWheel(
            selectedValue = selectedYear,
            range = datePickerHandler.getSelectableYearsRange(),
            dividersHeight = dividersHeight,
            dividersColor = dividersColor,
            textStyle = textStyle,
            onSelectedValueChange = { newYear ->
                selectedYear = newYear
                onSelectedDateChange(
                    SelectedJalaliDate(
                        year = selectedYear,
                        month = selectedMonth,
                        day = selectedDay
                    )
                )
            },
            modifier = Modifier.weight(0.25f)
        )

        CalendarPartWheel(
            selectedValue = selectedMonth,
            range = datePickerHandler.getSelectableMonthsRange(selectedYear),
            dividersHeight = dividersHeight,
            dividersColor = dividersColor,
            textStyle = textStyle,
            onSelectedValueChange = { newMonth ->
                selectedMonth = newMonth
                onSelectedDateChange(
                    SelectedJalaliDate(
                        year = selectedYear,
                        month = selectedMonth,
                        day = selectedDay
                    )
                )
            },
            label = { value ->
                val monthName = datePickerHandler.getMonthName(value).asString(context)
                if (showMonthNumber) {
                    val monthNumber = value.toString()
                    "$monthName / $monthNumber"
                } else monthName
            },
            modifier = Modifier.weight(0.5f)
        )

        CalendarPartWheel(
            selectedValue = selectedDay,
            range = datePickerHandler.getSelectableDaysRange(selectedMonth, selectedYear),
            dividersHeight = dividersHeight,
            dividersColor = dividersColor,
            textStyle = textStyle,
            onSelectedValueChange = { newDay ->
                selectedDay = newDay
                onSelectedDateChange(
                    SelectedJalaliDate(
                        year = selectedYear,
                        month = selectedMonth,
                        day = selectedDay
                    )
                )
            },
            modifier = Modifier.weight(0.25f)
        )
    }
}