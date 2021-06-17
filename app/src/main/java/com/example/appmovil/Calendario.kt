package com.example.appmovil

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.TextView
import java.util.*

class Calendario : AppCompatActivity() {
    lateinit var calendarView: CalendarView
    lateinit var dateView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario)

        title = "Calendario"
        calendarView = findViewById(R.id.calendarView)
        dateView = findViewById(R.id.dateView)
        calendarView.setOnDateChangeListener(CalendarView.OnDateChangeListener { calendarView: CalendarView, year: Int, month: Int, dayOfMonth: Int ->
            val date = dayOfMonth.toString() + "−" + (month + 1) + "−" + year
            dateView.text = date
        })
    }

    fun addCalendarEvent(view: View) {
        val calendarEvent: Calendar = Calendar.getInstance()
        val intent = Intent(Intent.ACTION_EDIT)
        intent.type = "vnd.android.cursor.item/event"
        intent.putExtra("beginTime", calendarEvent.timeInMillis)
        intent.putExtra("allDay", true)
        intent.putExtra("rule", "FREQ=YEARLY")
        intent.putExtra("endTime", calendarEvent.timeInMillis + 60 * 60 * 1000)
        intent.putExtra("title", "Calendar Event")
        startActivity(intent)
    }
}