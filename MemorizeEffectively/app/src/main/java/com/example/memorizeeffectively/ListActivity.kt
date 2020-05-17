package com.example.memorizeeffectively

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import io.realm.RealmResults

import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.content_list.*
import kotlinx.android.synthetic.main.fragment_alarm.*

class ListActivity : AppCompatActivity(), ItemClickListener, TimePickerFragment.onTimeSetListener,
    DatePickerFragment.onDateSetListener, AlarmFragment.OnSaveAlarmInfoListener {

    lateinit var realm : Realm
    lateinit var results : RealmResults<RealmModel>

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            goEdit("", "", false, Mode.NEW_WORD.name)
        }
    }

    override fun onResume() {
        super.onResume()
        realm = Realm.getDefaultInstance()
        results = realm.where(RealmModel::class.java).findAll()

        upDate()
        alarmReceiver()
    }

    private fun alarmReceiver() {

        alarmMgr = context.getSystemService(Context.ALARM_SERVICE)as AlarmManager

        //alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        //    alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
        //        PendingIntent.getBroadcast(context, 0, intent, 0)
        //    }
        //
        //    alarmMgr?.set(
        //            AlarmManager.ELAPSED_REALTIME_WAKEUP,
        //            SystemClock.elapsedRealtime() + 60 * 1000,
        //            alarmIntent
        //    )
    }

    override fun onPause() {
        super.onPause()
        realm.close()
    }

    private fun upDate() {

        val mAdapter = RecyclerviewAdapterList(results, this)
        val mManager = LinearLayoutManager(this)
        my_recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = mManager
            adapter = mAdapter
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.menu_done).isVisible = false
        menu.findItem(R.id.menu_back).isVisible = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.menu_back -> {
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun goEdit(word : String, definition : String, ifMemorized : Boolean, mode: String) {
        val intent = Intent(this@ListActivity, EditActivity::class.java).apply {
            putExtra(IntentKey.WORD.name, word)
            putExtra(IntentKey.DEFINITION.name, definition)
            putExtra(IntentKey.IF_MEMORIZED.name, ifMemorized)
            putExtra(IntentKey.MODE.name, mode)
        }
        startActivity(intent)
    }

    //Interface
    override fun onItemClickChange(item: RealmModel) {
        realm.beginTransaction()
        val selectedItem = realm.where(RealmModel::class.java)
            .equalTo("word",item.word).equalTo("definition", item.definition).findFirst()
        when(item.statusWords){
            StatusWords.STATUS_WORD.name -> selectedItem!!.statusWords = StatusWords.STATUS_DEFINITION.name
            StatusWords.STATUS_DEFINITION.name -> selectedItem!!.statusWords = StatusWords.STATUS_WORD.name
        }
        upDate()
    }

    //Interface
    override fun onItemClickEdit(item: RealmModel) {
        goEdit(item.word, item.definition, item.ifMemorized, Mode.EDIT.name)
    }

    //Interface
    override fun onItemClickAlarm(item : RealmModel) {
        openAlarmFragment(item.word, item. definition, item.dateString, item.timeString)
    }

    private fun openAlarmFragment(dateString : String, timeString : String, word: String, definition: String) {
        supportFragmentManager.beginTransaction().add(
            R.id.alarm_container,
            AlarmFragment.newInstance(dateString, timeString, word, definition)
        ).commit()
    }

    //Interface
    override fun onTimeSet(timeString: String) {
        textInputTime.setText(timeString)
    }

    //Interface
    override fun onDateSet(dateString: String) {
        textInputDate.setText((dateString))
    }

    //AlarmFragment.OnSaveAlarmInfoListener
    override fun onSaveAlarm() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    //AlarmFragment.OnSaveAlarmInfoListener
    override fun callDatePickerFragment() {
        DatePickerFragment().show(supportFragmentManager, "datePicker")
    }
    //AlarmFragment.OnSaveAlarmInfoListener
    override fun callTimePickerFragment() {
        TimePickerFragment().show(supportFragmentManager, "timePicker")
    }


}
