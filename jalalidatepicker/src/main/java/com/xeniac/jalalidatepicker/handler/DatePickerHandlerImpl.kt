package com.xeniac.jalalidatepicker.handler

import com.xeniac.jalalidatepicker.R
import com.xeniac.jalalidatepicker.models.DatePickerDefaults
import com.xeniac.jalalidatepicker.utils.JalaliCalendarUtil
import com.xeniac.jalalidatepicker.utils.UiText
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
internal class DatePickerHandlerImpl(
    private val selectableYearsRange: Iterable<Int>?,
    private val isSelectFromFutureEnabled: Boolean,
    private val datePickerDefaults: DatePickerDefaults
) : DatePickerHandler {

    private val currentDate = Clock.System.now()
    private val currentDateShamsi = JalaliCalendarUtil.apply {
        convertGregorianToJalali(currentDate)
    }

    private val maxMonth = if (isSelectFromFutureEnabled) 12 else currentDateShamsi.month
    private val maxDay = if (isSelectFromFutureEnabled) 31 else currentDateShamsi.day
    private val maxYear = when {
        selectableYearsRange != null -> selectableYearsRange.last()
        isSelectFromFutureEnabled -> currentDateShamsi.year + datePickerDefaults.selectableFutureYears
        else -> currentDateShamsi.year
    }

    override fun getMonthLength(
        currentMonth: Int,
        currentYear: Int
    ) = when (currentMonth) {
        12 -> if (JalaliCalendarUtil.isLeapYear(currentYear)) 30 else 29
        1, 2, 3, 4, 5, 6 -> 31
        else -> 30
    }

    override fun getLastPossibleDayOfMonth(
        currentMonth: Int,
        currentYear: Int
    ): Int {
        val monthLength = getMonthLength(currentMonth, currentYear)

        return if (currentYear == maxYear && currentMonth == maxMonth) {
            if (maxDay < monthLength) maxDay
            else monthLength
        } else monthLength
    }

    override fun getLastPossibleMonthOfYear(
        currentYear: Int
    ): Int = if (currentYear == maxYear) maxMonth else 12

    override fun getMonthName(month: Int): UiText = UiText.StringArrayResource(
        resId = R.array.jalali_months,
        index = month - 1
    )

    override fun getSelectableYearsRange(): Iterable<Int> {
        if (selectableYearsRange != null) {
            return selectableYearsRange
        }

        val yearsRange = if (isSelectFromFutureEnabled) {
            datePickerDefaults.selectablePastYears + datePickerDefaults.selectableFutureYears
        } else datePickerDefaults.selectablePastYears

        return maxYear - yearsRange..maxYear
    }

    override fun getSelectableMonthsRange(
        currentYear: Int,
    ): Iterable<Int> = 1..getLastPossibleMonthOfYear(currentYear)

    override fun getSelectableDaysRange(
        currentMonth: Int,
        currentYear: Int,
    ): Iterable<Int> = 1..getLastPossibleDayOfMonth(currentMonth, currentYear)
}