package com.xeniac.jalalidatepicker.domain

import com.xeniac.jalalidatepicker.utils.UiText

internal interface DatePickerRepository {

    /**
     * Gets the length (number of days) of a given month in the Jalali (Shamsi) calendar for a specific year.
     *
     * This function calculates the number of days in the specified month for the given year,
     * taking into account the leap year rules of the Jalali calendar.
     *
     * In the Jalali calendar:
     * - Months 1 to 6 have 31 days.
     * - Months 7 to 11 have 30 days.
     * - Month 12 (Esfand) has 29 days in a common year and 30 days in a leap year.
     *
     * @param currentMonth The month number (1 to 12) in the Jalali calendar.
     * @param currentYear The year in the Jalali calendar.
     * @return The number of days in the specified month for the given year.
     *
     * @see com.xeniac.jalalidatepicker.utils.JalaliCalendarUtil.isLeapYear
     */
    fun getMonthLength(
        currentMonth: Int,
        currentYear: Int
    ): Int

    /**
     * Gets the last possible day of a given month in the Jalali (Shamsi) calendar for a specific year.
     *
     * This function determines the last valid day for the specified month and year,
     * considering both the standard length of the month and any potential constraints
     * imposed by the `maxYear`, `maxMonth`, and `maxDay` properties.
     *
     * In most cases, this will be the standard last day of the month (e.g., 30 or 31).
     * However, if the `currentYear` and `currentMonth` match the `maxYear` and `maxMonth`,
     * respectively, then the last possible day might be earlier than the standard last day
     * if `maxDay` is less than the standard month length.
     *
     * For example, if `maxYear` is 1403, `maxMonth` is 5, and `maxDay` is 15, then for
     * `currentYear = 1403` and `currentMonth = 5`, this function will return 15,
     * even though month 5 normally has 31 days.
     *
     * @param currentMonth The month number (1 to 12) in the Jalali calendar.
     * @param currentYear The year in the Jalali calendar.
     * @return The last possible day of the specified month for the given year.
     *
     * @see getMonthLength
     */
    fun getLastPossibleDayOfMonth(
        currentMonth: Int,
        currentYear: Int
    ): Int

    /**
     * Gets the last possible month of the year in the Jalali (Shamsi) calendar for a given year.
     *
     * This function determines the last valid month for the specified year,
     * considering any potential constraints imposed by the `maxYear` and `maxMonth` properties.
     *
     * In most cases, this will be the standard last month of the year (12).
     * However, if the `currentYear` matches the `maxYear`, then the last possible month
     * might be earlier than 12 if `maxMonth` is less than 12.
     *
     * For example, if `maxYear` is 1403 and `maxMonth` is 5, then for `currentYear = 1403`,
     * this function will return 5, even though a normal year has 12 months.
     *
     * @param currentYear The year in the Jalali calendar.
     * @return The last possible month of the year.
     */
    fun getLastPossibleMonthOfYear(currentYear: Int): Int

    /**
     * Gets the name of a month in the Jalali (Shamsi) calendar.
     *
     * This function retrieves the name of the specified month from a string array resource.
     * The month names are expected to be defined in the `jalali_months` string array
     * resource in the application's resources.
     *
     * @param month The month number (1 to 12) in the Jalali calendar.
     * @return A [UiText] object representing the name of the month.
     *   This [UiText] is a [UiText.StringArrayResource] that encapsulates the resource ID
     *   and the index of the month name within the string array.
     *
     * @throws IllegalArgumentException if the provided month is not between 1 and 12.
     */
    fun getMonthName(month: Int): UiText

    /**
     * Gets the range of selectable years in the Jalali (Shamsi) calendar.
     *
     * This function determines the range of years that are valid for selection in the date picker.
     * The range is calculated based on the following factors:
     *
     * 1.  **`selectableYearsRange`:** If this property is not null, it directly defines the selectable year range.
     * 2.  **`isSelectFromFutureEnabled`:** If this is true, the range will extend into the future.
     * 3.  **`datePickerDefaults.selectablePastYears`:** This determines the number of selectable years in the past.
     * 4.  **`datePickerDefaults.selectableFutureYears`:** If `isSelectFromFutureEnabled` is true, this determines the number of selectable years in the future.
     * 5.  **`maxYear`:** This is the upper bound of the selectable year range.
     *
     * If `selectableYearsRange` is provided, it will be used directly. Otherwise, the range will be calculated as follows:
     * -   If `isSelectFromFutureEnabled` is true, the range will be from `maxYear - (selectablePastYears + selectableFutureYears)` to `maxYear`.
     * -   If `isSelectFromFutureEnabled` is false, the range will be from `maxYear - selectablePastYears` to `maxYear`.
     *
     * @return An [Iterable] of [Int] representing the selectable year range.
     *
     * @see com.xeniac.jalalidatepicker.models.DatePickerDefaults
     */
    fun getSelectableYearsRange(): Iterable<Int>

    /**
     * Gets the range of selectable months for a given year in the Jalali (Shamsi) calendar.
     *
     * This function determines the valid range of months that can be selected for the specified `currentYear`.
     * The range always starts from month 1 (Farvardin) and
     * extends up to the last possible month of the year, which is determined by [getLastPossibleMonthOfYear].
     *
     * In most cases, the range will be from 1 to 12. However, if there are constraints
     * that limit the selectable months (e.g., if the date picker is configured to
     * only allow selection up to a certain month), then the upper bound of the range
     * might be less than 12.
     *
     * @param currentYear The year in the Jalali calendar for which to get the month range.
     * @return An [Iterable] of [Int] representing the selectable month range (1 to the last possible month).
     *
     * @see getLastPossibleMonthOfYear
     */
    fun getSelectableMonthsRange(
        currentYear: Int
    ): Iterable<Int>

    /**
     * Gets the range of selectable days for a given month and year in the Jalali (Shamsi) calendar.
     *
     * This function determines the valid range of days that can be selected for the
     * specified `currentMonth` and `currentYear`.
     * The range always starts from day 1
     * and extends up to the last possible day of the month, which is determined by [getLastPossibleDayOfMonth].
     *
     * The length of the month can vary depending on the month and whether the year is a leap year.
     * Additionally, constraints might limit the selectable days (e.g., if the date picker is
     * configured to only allow selection up to a certain day). In such cases, the upper
     * bound of the range might be less than the standard month length.
     *
     * @param currentMonth The month number (1 to 12) in the Jalali calendar.
     * @param currentYear The year in the Jalali calendar.
     * @return An [Iterable] of [Int] representing the selectable day range (1 to the last possible day).
     *
     * @see getLastPossibleDayOfMonth
     */
    fun getSelectableDaysRange(
        currentMonth: Int,
        currentYear: Int
    ): Iterable<Int>
}