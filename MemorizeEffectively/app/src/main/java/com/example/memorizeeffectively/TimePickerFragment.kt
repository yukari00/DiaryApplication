package com.example.memorizeeffectively

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.lang.RuntimeException
import java.text.SimpleDateFormat
import java.util.*


class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private var listener : onTimeSetListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context == onTimeSetListener){
            listener = context
        }else{
            throw RuntimeException(context.toString() + "must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface onTimeSetListener{
        fun onTimeSet(timeString : String)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val timeString = getTimeString(hourOfDay, minute)
        listener!!.onTimeSet(timeString)
        fragmentManager?.beginTransaction()!!.remove(this).commit()
    }

    private fun getTimeString(hourOfDay: Int, minute: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(hourOfDay, minute)
        val timeFormat = SimpleDateFormat("kk:mm:ss")
        return timeFormat.format(calendar.time)

    }

}

//override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
//        val dateString = getDateString(year, month, dayOfMonth)
//        listener?.onDateSelected(dateString)
//        fragmentManager?.beginTransaction()!!.remove(this).commit()
//
//    }
//
//    private fun getDateString(year: Int, month: Int, dayOfMonth: Int): String {
//        val calendar = Calendar.getInstance()
//        calendar.set(year, month, dayOfMonth)
//        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
//        return dateFormat.format(calendar.time)
