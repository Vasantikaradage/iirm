package com.indiainsure.android.MB360.monthpicker

import android.util.SparseIntArray
import com.aminography.primedatepicker.common.BackgroundShapeType
import com.aminography.primedatepicker.picker.theme.LightThemeFactory
import com.indiainsure.android.MB360.R
import java.util.Calendar

class DatePickerTheme : LightThemeFactory() {

    companion object {
        public fun getTheme(): LightThemeFactory {
            val themeFactory = object : LightThemeFactory() {


                
                override val dialogBackgroundColor: Int
                    get() = getColor(R.color.white)

                override val calendarViewBackgroundColor: Int
                    get() = getColor(R.color.white)

                override val pickedDayBackgroundShapeType: BackgroundShapeType
                    get() = BackgroundShapeType.CIRCLE

                //picked day
                override val calendarViewPickedDayBackgroundColor: Int
                    get() = getColor(R.color.greenlightbg2)

                override val calendarViewPickedDayInRangeBackgroundColor: Int
                    get() = getColor(R.color.greenlightbg2)

                override val calendarViewPickedDayInRangeLabelTextColor: Int
                    get() = getColor(R.color.white)

                override val calendarViewTodayLabelTextColor: Int
                    get() = getColor(R.color.greenlightbg2)


                override val calendarViewWeekLabelTextColors: SparseIntArray
                    get() = SparseIntArray(7).apply {
                        val red = getColor(R.color.red)
                        val green = getColor(R.color.greenlightbg2)
                        put(Calendar.SATURDAY, green)
                        put(Calendar.SUNDAY, red)
                        put(Calendar.MONDAY, green)
                        put(Calendar.TUESDAY, green)
                        put(Calendar.WEDNESDAY, green)
                        put(Calendar.THURSDAY, green)
                        put(Calendar.FRIDAY, green)
                    }

                override val calendarViewShowAdjacentMonthDays: Boolean
                    get() = false

                override val selectionBarBackgroundColor: Int
                    get() = getColor(R.color.greenlightbg2)

                override val selectionBarRangeDaysItemBackgroundColor: Int
                    get() = getColor(R.color.black)

                override val calendarViewMonthLabelTextColor: Int
                    get() = getColor(R.color.black)

                override val actionBarPositiveTextColor: Int
                    get() =getColor(R.color.greenlightbg2)

                override val selectionBarMultipleDaysItemBackgroundColor: Int
                    get() = getColor(R.color.greenlightbg2)

                override val gotoViewBackgroundColor: Int
                    get() = getColor(R.color.greenlightbg2)

            }


            return themeFactory;
        }

        public fun getEnrollmentTheme(): LightThemeFactory {
            val themeFactory = object : LightThemeFactory() {



                override val dialogBackgroundColor: Int
                    get() = getColor(R.color.white)

                override val calendarViewBackgroundColor: Int
                    get() = getColor(R.color.white)

                override val pickedDayBackgroundShapeType: BackgroundShapeType
                    get() = BackgroundShapeType.CIRCLE

                //picked day
                override val calendarViewPickedDayBackgroundColor: Int
                    get() = getColor(R.color.gradient_start)

                override val calendarViewPickedDayInRangeBackgroundColor: Int
                    get() = getColor(R.color.gradient_start)

                override val calendarViewPickedDayInRangeLabelTextColor: Int
                    get() = getColor(R.color.white)

                override val calendarViewTodayLabelTextColor: Int
                    get() = getColor(R.color.gradient_start)


                override val calendarViewWeekLabelTextColors: SparseIntArray
                    get() = SparseIntArray(7).apply {
                        val red = getColor(R.color.gradient_start)
                        val green = getColor(R.color.gradient_start)
                        put(Calendar.SATURDAY, green)
                        put(Calendar.SUNDAY, red)
                        put(Calendar.MONDAY, green)
                        put(Calendar.TUESDAY, green)
                        put(Calendar.WEDNESDAY, green)
                        put(Calendar.THURSDAY, green)
                        put(Calendar.FRIDAY, green)
                    }

                override val calendarViewShowAdjacentMonthDays: Boolean
                    get() = false

                override val selectionBarBackgroundColor: Int
                    get() = getColor(R.color.gradient_start)

                override val selectionBarRangeDaysItemBackgroundColor: Int
                    get() = getColor(R.color.black)

                override val calendarViewMonthLabelTextColor: Int
                    get() = getColor(R.color.black)

                override val actionBarPositiveTextColor: Int
                    get() =getColor(R.color.gradient_start)

                override val selectionBarMultipleDaysItemBackgroundColor: Int
                    get() = getColor(R.color.gradient_start)

                override val gotoViewBackgroundColor: Int
                    get() = getColor(R.color.gradient_start)

            }


            return themeFactory;
        }
    }
}