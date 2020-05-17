package com.example.memorizeeffectively

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import io.realm.Realm

import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.content_edit.*

class EditActivity : AppCompatActivity() {

    lateinit var realm : Realm
    var mode = ""
    var definition = ""
    var word = ""
    var ifMemorized = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setSupportActionBar(toolbar)

        word_edit.setText(word)
        definition_edit.setText(definition)

        val bundle = intent.extras!!
        word = bundle.getString(IntentKey.WORD.name)!!
        definition = bundle.getString(IntentKey.DEFINITION.name)!!
        mode = bundle.getString(IntentKey.MODE.name)!!
        ifMemorized = bundle.getBoolean(IntentKey.IF_MEMORIZED.name)


        when (mode) {
            Mode.NEW_WORD.name -> {
                checkBox.isVisible = false
                checkBox.isEnabled = false
            }
            else -> {
                checkBox.isEnabled = true
                checkBox.isChecked = ifMemorized
            }
        }
    }

    override fun onResume() {
        super.onResume()

        realm = Realm.getDefaultInstance()

    }

    override fun onPause() {
        super.onPause()

        realm.close()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.menu_done).isVisible = true
        menu.findItem(R.id.menu_back).isVisible = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.menu_done -> {
                check()
            }
            R.id.menu_back -> {
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun check(): Boolean {
        if(word_edit.text.toString()=="") return false
        if(definition_edit.text.toString()=="") return false
        when(mode){
            Mode.NEW_WORD.name -> addNewWord()
            else -> editExistingWord()
        }
        return true
    }


    private fun editExistingWord(): Boolean {
        val selectedItem = realm.where(RealmModel::class.java).equalTo("word", word).
            equalTo("definition", definition).findFirst()!!

        val dialog = AlertDialog.Builder(this@EditActivity).apply {
            setTitle(R.string.edit_title_alert)
            setMessage(R.string.edit_message_alert)
            setPositiveButton(R.string.edit_title_alert){dialog, which ->
                realm.beginTransaction()
                selectedItem.apply {
                    word = word_edit.text.toString()
                    definition = definition_edit.text.toString()
                    ifMemorized = checkBox.isChecked
                    dateString = ""
                    timeString = ""
                }

                realm.commitTransaction()
            }
            setNegativeButton(R.string.alert_dialog_cancel){dialog, which ->  }
            show()
        }

        return true
    }

    private fun addNewWord(): Boolean {

        val dialog = AlertDialog.Builder(this@EditActivity).apply {
            setTitle(R.string.save_titile_alert)
            setMessage(R.string.save_message_alert)
            setPositiveButton(R.string.save_titile_alert){dialog, which ->
                realm.beginTransaction()
                val realmModel = realm.createObject(RealmModel::class.java)
                realmModel.apply {
                    word = word_edit.text.toString()
                    definition = definition_edit.text.toString()
                    ifMemorized = false
                    dateString = ""
                    timeString = ""

                }
                realm.commitTransaction()
            }
            setNegativeButton(R.string.alert_dialog_cancel){dialog, which ->  }
            show()
        }
        return true
    }
}
