package com.xeniac.jalalidatepicker.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.Locale

@Suppress("FunctionName", "LocalVariableName", "SameParameterValue")
internal object JalaliCalendarUtil {

    var day: Int = 0
        private set
    var month: Int = 0
        private set
    var year: Int = 0
        private set

    private var jalaliYear = 0
    private var jalaliMonth = 0
    private var jalaliDay = 0

    private var gregYear = 0
    private var gregMonth = 0
    private var gregDay = 0

    private var leap = 0
    private var march = 0

    /**
     * Calculates the Julian Day number (JG2JD) from Gregorian or Julian calendar dates.
     * This integer number corresponds to the noon of the date (i.e. 12 hours of Universal Time).
     *
     * The procedure was tested to be good since 1 March, -100100 (of both the calendars)
     * up to a few millions (10**6) years into the future.
     *
     * The algorithm is based on D.A. Hatcher, Q.Jl.R.Astron.Soc. 25(1984), 53-55
     * slightly modified by me (K.M.Borkowski, Post.Astron. 25(1987), 275-279).
     *
     * @param year
     * @param month
     * @param day
     * @param j1G0 To be set to 1 for Julian and to 0 for Gregorian calendar
     * @return Julian Day number
     */
    private fun JG2JD(
        year: Int,
        month: Int,
        day: Int,
        j1G0: Int
    ): Int {
        var jd = ((1461 * (year + 4800 + (month - 14) / 12)) / 4
                + (367 * (month - 2 - 12 * ((month - 14) / 12))) / 12
                - (3 * ((year + 4900 + (month - 14) / 12) / 100)) / 4 + day
                - 32075)
        if (j1G0 == 0) {
            jd = jd - (year + 100100 + (month - 8) / 6) / 100 * 3 / 4 + 752
        }
        return jd
    }

    /**
     * Calculates Gregorian and Julian calendar dates from the Julian Day number (JD)
     * for the period since JD=-34839655 (i.e. the year -100100 of both the calendars)
     * to some millions (10**6) years ahead of the present.
     *
     * The algorithm is based on D.A. Hatcher, Q.Jl.R.Astron.Soc. 25(1984), 53-55
     * slightly modified by me (K.M. Borkowski, Post.Astron. 25(1987), 275-279).
     *
     * @param JD Julian day number as int
     * @param J1G0 to be set to 1 for Julian and to 0 for Gregorian calendar
     */
    private fun JD2JG(
        JD: Int,
        J1G0: Int
    ) {
        val i: Int
        var j: Int
        j = 4 * JD + 139361631
        if (J1G0 == 0) {
            j = j + (4 * JD + 183187720) / 146097 * 3 / 4 * 4 - 3908
        }
        i = (j % 1461) / 4 * 5 + 308
        gregDay = (i % 153) / 5 + 1
        gregMonth = ((i / 153) % 12) + 1
        gregYear = j / 1461 - 100100 + (8 - gregMonth) / 6
    }

    /**
     * Converts the Julian Day number to a date in the Jalali calendar.
     *
     * @param JDN the Julian Day number
     */
    private fun jD2Jal(JDN: Int) {
        JD2JG(JD = JDN, J1G0 = 0)

        jalaliYear = gregYear - 621
        JalCal(jY = jalaliYear)

        val JDN1F = JG2JD(
            year = gregYear,
            month = 3,
            day = march,
            j1G0 = 0
        )

        var k = JDN - JDN1F
        if (k >= 0) {
            if (k <= 185) {
                jalaliMonth = 1 + k / 31
                jalaliDay = (k % 31) + 1
                return
            } else {
                k -= 186
            }
        } else {
            jalaliYear -= 1
            k += 179
            if (leap == 1) {
                k += 1
            }
        }

        jalaliMonth = 7 + k / 30
        jalaliDay = (k % 30) + 1
    }

    /**
     * Converts a date of the Jalali calendar to the Julian Day Number.
     *
     * @param jY Jalali year
     * @param jM Jalali month
     * @param jD Jalali day
     * @return Julian day number
     */
    private fun Jal2JD(
        jY: Int,
        jM: Int,
        jD: Int
    ): Int {
        JalCal(jY = jY)

        return JG2JD(
            year = gregYear,
            month = 3,
            day = march,
            j1G0 = 1
        ) + (jM - 1) * 31 - jM / 7 * (jM - 7) + jD - 1
    }

    /**
     * This procedure determines if the Jalali (Persian) year is leap (366-day long) or is the common year (365 days),
     * and finds the day in March (Gregorian calendar) of the first day of the Jalali year (jYear).
     *
     * @param jY Jalali calendar year (-61 to 3177)
     */
    private fun JalCal(jY: Int) {
        march = 0
        leap = 0
        val breaks = intArrayOf(
            -61, 9, 38, 199, 426, 686, 756, 818, 1111, 1181, 1210,
            1635, 2060, 2097, 2192, 2262, 2324, 2394, 2456, 3178
        )
        gregYear = jY + 621
        var leapJ = -14
        var jp = breaks[0]
        var jump: Int
        for (j in 1..19) {
            val jm = breaks[j]
            jump = jm - jp
            if (jY < jm) {
                var N = jY - jp
                leapJ += N / 33 * 8 + (N % 33 + 3) / 4
                if ((jump % 33) == 4 && (jump - N) == 4) {
                    leapJ += 1
                }
                val leapG = (gregYear / 4) - (gregYear / 100 + 1) * 3 / 4 - 150
                march = 20 + leapJ - leapG
                if ((jump - N) < 6) {
                    N = N - jump + (jump + 4) / 33 * 33
                }
                leap = ((((N + 1) % 33) - 1) % 4)
                if (leap == -1) {
                    leap = 4
                }
                break
            }
            leapJ += jump / 33 * 8 + (jump % 33) / 4
            jp = jm
        }
    }

    /**
     * Determines whether a given year is a leap year in the Jalali (Shamsi) calendar.
     *
     * This function checks if the provided year,
     * is a leap year according to the Jalali calendar's leap year rules.
     *
     * The Jalali calendar has a 33-year cycle, and the leap years within this cycle are determined by
     * specific remainders when the year is divided by 33.
     *
     * @param year The year to be checked
     * @return `true` if the year is a leap year, `false` otherwise.
     *
     * @see <a href="https://en.wikipedia.org/wiki/Jalali_calendar">Jalali calendar on Wikipedia</a>
     */
    fun isLeapYear(year: Int): Boolean = when (year % 33) {
        1, 5, 9, 13, 17, 22, 26, 30 -> true
        else -> false
    }

    /**
     * Modified toString() method that represents date as string.
     *
     * @return Date as String
     */
    override fun toString(): String {
        return String.format(
            /* locale = */ Locale.getDefault(),
            /* format = */ "%04d-%02d-%02d",
            year,
            month,
            day
        )
    }

    /**
     * Converts Gregorian date to Persian(Jalali) date.
     *
     * @param gregYear
     * @param gregMonth
     * @param gregDay
     */
    fun convertGregorianToJalali(
        gregYear: Int,
        gregMonth: Int,
        gregDay: Int
    ) {
        val jd = JG2JD(
            year = gregYear,
            month = gregMonth,
            day = gregDay,
            j1G0 = 0
        )
        jD2Jal(JDN = jd)

        this.year = jalaliYear
        this.month = jalaliMonth
        this.day = jalaliDay
    }

    fun convertGregorianToJalali(
        date: Instant,
        timeZone: TimeZone = TimeZone.currentSystemDefault()
    ) {
        val localDateTime = date.toLocalDateTime(timeZone = timeZone)
        convertGregorianToJalali(
            localDateTime.year,
            localDateTime.monthNumber,
            localDateTime.dayOfMonth
        )
    }

    /**
     * Converts Persian(Jalali) date to Gregorian date
     *
     * @param year
     * @param month
     * @param day
     */
    fun convertJalaliToGregorian(
        year: Int,
        month: Int,
        day: Int
    ) {
        val jd = Jal2JD(year, month, day)
        JD2JG(jd, 0)
        this.year = gregYear
        this.month = gregMonth
        this.day = gregDay
    }
}