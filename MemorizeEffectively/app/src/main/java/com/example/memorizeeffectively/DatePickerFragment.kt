package com.example.memorizeeffectively

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.lang.RuntimeException
import java.text.SimpleDateFormat
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var listener : onDateSetListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context == onDateSetListener){
            listener = context
        }else{
            throw RuntimeException(context.toString() + "must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface onDateSetListener{
        fun onDateSet(dateString : String)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireActivity(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val dateString: String = getDateString(year, month, dayOfMonth)
        listener!!.onDateSet(dateString)
        parentFragmentManager.beginTransaction().remove(this).commit()
    }

    private fun getDateString(year: Int, month: Int, dayOfMonth: Int): String {

        val c = Calendar.getInstance()
        c.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
        return dateFormat.format(c.time)
    }
}