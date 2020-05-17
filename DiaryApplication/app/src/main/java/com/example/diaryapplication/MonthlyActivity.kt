package com.example.diaryapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.CalendarView
import android.widget.LinearLayout
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_monthly.*
import kotlinx.android.synthetic.main.content_monthly.*
import java.text.SimpleDateFormat
import java.util.*

class MonthlyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly)
        setSupportActionBar(toolbar)

        val dateFormat = SimpleDateFormat("yyyy年MM月dd日", Locale.JAPAN)
        val todayDate = calendarView.date
        val todayString = dateFormat.format(todayDate)

        val maxDate = calendarView.maxDate
        calendarView.maxDate = calendarView.date

        //Todo 日記を書いた日はカレンダーにチェックが入っている

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val date : String = "${year}年${month}月${dayOfMonth}日"

            supportFragmentManager.beginTransaction()
                .add(R.id.frameLayout, MonthlyClickedFragment.newInstance(date)).commit()
        }

    }


}
