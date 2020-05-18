package com.example.diaryapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.content_edit.toolbar
import kotlinx.android.synthetic.main.activity_edit.drawerLayout
import kotlinx.android.synthetic.main.activity_edit.navView
import kotlinx.android.synthetic.main.content_edit.*

class EditActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()
    var todayString : String? = ""
    var todayDetail : String? = ""
    var mState : Int = 0
    val ADD_NEW_DAIRY = 1
    val UPDATE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        val bundle = intent.extras!!
        todayString = bundle.getString(IntentKey.DATE.name)
        todayDetail = bundle.getString(IntentKey.TODAY_DETAIL.name)

        mState = if(todayDetail == ""){
            ADD_NEW_DAIRY
        }else{
            UPDATE
        }

        editTextTodayDetail.setText(todayDetail)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        menu!!.apply {
            findItem(R.id.menu_delete).isVisible = true
            findItem(R.id.menu_edit).isVisible = false
            findItem(R.id.menu_done).isVisible = true
            findItem(R.id.menu_photo).isVisible = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_delete -> deleteData()
            R.id.menu_done -> saveDiary()
            R.id.menu_photo -> insertPhoto()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertPhoto() {

    }

    private fun saveDiary() {

        val newTodayDetail : String? = editTextTodayDetail.text.toString()

        if(newTodayDetail == ""){
            editTextTodayDetail.error = "入力してください"
            return
        }
        //setメソッドだと全体が上書きされるからupdateとaddで分ける必要なし
        val newData = DiaryData(todayString, newTodayDetail)
        database.collection(CollectionName.MY_DIARY.name).document(todayString!!).set(newData)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }

        startActivity(Intent(this@EditActivity, DailyActivity::class.java))
        finish()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_profile -> {
                Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_today -> {
                startActivity(Intent(this@EditActivity, DailyActivity::class.java))
            }
            R.id.nav_thisWeek -> {
                startActivity(Intent(this@EditActivity, WeeklyActivity::class.java))
            }
            R.id.nav_thisMonth -> {
                startActivity(Intent(this@EditActivity, MonthlyActivity::class.java))
            }
            R.id.nav_update -> {
                Toast.makeText(this, "Update clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_logout -> {
                signOut()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        finish()
        return true
    }

    private fun signOut() {
        startActivity(MainActivity.getLaunchIntent(this))
        FirebaseAuth.getInstance().signOut();
    }

    private fun deleteData() {
        //Todo deleteData処理
    }

    companion object{
        private val TAG = "EditActivity"
    }
}
