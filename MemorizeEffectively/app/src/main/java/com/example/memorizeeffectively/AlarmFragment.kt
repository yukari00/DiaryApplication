package com.example.memorizeeffectively

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_alarm.*
import kotlinx.coroutines.selects.select
import java.lang.RuntimeException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private val ARG_DATESTRING = IntentKey.DATE_STRING.name
private val ARG_TIMESTRING = IntentKey.TIME_STRING.name
private val ARG_WORD = IntentKey.WORD.name
private val ARG_DEFINITION = IntentKey.DEFINITION.name

/**
 * A simple [Fragment] subclass.
 * Use the [AlarmFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlarmFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var dateString: String? = ""
    private var timeString: String? = ""
    private var word: String? = ""
    private var definition: String? = ""

    private var listener : OnSaveAlarmInfoListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context == OnSaveAlarmInfoListener){
            listener = context
        }else{
            throw RuntimeException(context.toString() + "must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dateString = it.getString(ARG_DATESTRING)
            timeString = it.getString(ARG_TIMESTRING)
            word = it.getString(ARG_WORD)
            definition = it.getString(ARG_DEFINITION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_alarm, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.apply {
            findItem(R.id.menu_done).isVisible = true
            findItem(R.id.menu_back).isVisible = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_back -> parentFragmentManager.beginTransaction().remove(this).commit()
            R.id.menu_done -> {
                saveAlarmInfo()
                listener!!.onSaveAlarm()
                parentFragmentManager.beginTransaction().remove(this).commit()
            }
        }
        return super.onOptionsItemSelected(item)

    }

    private fun saveAlarmInfo() {
        val realm = Realm.getDefaultInstance()
        val selectedData = realm.where(RealmModel::class.java).equalTo("word", word)
            .equalTo("definition", definition).findFirst()!!
        realm.beginTransaction()
        selectedData.dateString = textInputDate.text.toString()
        selectedData.timeString = textInputTime.text.toString()
        realm.commitTransaction()
        realm.close()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        textInputDate.setText(dateString)
        textInputTime.setText(timeString)

        imageViewCalendar.setOnClickListener{ listener!!.callDatePickerFragment()}
        imageViewClock.setOnClickListener { listener!!.callTimePickerFragment() }
    }

    interface OnSaveAlarmInfoListener {
        fun onSaveAlarm()
        fun callDatePickerFragment()
        fun callTimePickerFragment()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AlarmFragment.
         */

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(word: String, definition: String, dateString: String, timeString: String) =
            AlarmFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_WORD, word)
                    putString(ARG_DEFINITION, definition)
                    putString(ARG_DATESTRING, dateString)
                    putString(ARG_TIMESTRING, timeString)
                }
            }
    }
}

