package com.example.diaryapplication

import android.content.Context
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
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.android.synthetic.main.activity_daily.*
import kotlinx.android.synthetic.main.content_daily.*

import java.text.SimpleDateFormat
import java.util.*

class DailyActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()
    var getDetail: String? = null
    var todayString: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        val calendar = Calendar.getInstance()
        val today = calendar.time
        val dateFormat = SimpleDateFormat("yyyy年MM月dd日")
        todayString = dateFormat.format(today)


        getDetail = database.collection(CollectionName.MY_DIARY.name).document(todayString).get().result?.get("detail").toString()


        upDate()
       }


    override fun onResume() {
           super.onResume()
           upDate()

       }

    private fun upDate() {

           todayDate.text = todayString

               if(getDetail != null){
                   todayDetail.text = getDetail
               }else{
                   todayDetail.text = ""// 出来事表示 (もし出来事なかったら→TodayDetail = "")
               }

       }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
           val inflater = menuInflater
           inflater.inflate(R.menu.menu_main, menu)
           menu!!.apply {
               findItem(R.id.menu_delete).isVisible = true
               findItem(R.id.menu_edit).isVisible = true
               findItem(R.id.menu_done).isVisible = false
           }
           return super.onCreateOptionsMenu(menu)
       }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
           when(item.itemId) {
               R.id.menu_edit -> if (todayDetail.text == "") {
                   goEdit(todayString, "")
               } else {
                   goEdit(todayString, "取得したデータのTodayDetail")
               }
               R.id.menu_delete -> deleteData()
           }

           return super.onOptionsItemSelected(item)
       }

    private fun deleteData() {

           if(getDetail != null){
               database.collection(CollectionName.MY_DIARY.name)
                   .document(todayString).delete()
                   .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully deleted!") }
                   .addOnFailureListener { e -> Log.w("TAG", "Error deleting document", e) }
           }else{
               Toast.makeText(this@DailyActivity, "中身がありません", Toast.LENGTH_SHORT).show()
           }

       }

    private fun goEdit(todayString : String, todayDetail : String) {
           val intent = Intent(this@DailyActivity, EditActivity::class.java).apply {
               putExtra(IntentKey.DATE.name, todayString)
               putExtra(IntentKey.TODAY_DETAIL.name, todayDetail)
           }
           startActivity(intent)
           finish()
       }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
           when(item.itemId){
               R.id.nav_profile -> {
                   Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
               }
               R.id.nav_today -> {

               }
               R.id.nav_thisWeek -> {
                   startActivity(Intent(this@DailyActivity, WeeklyActivity::class.java))
               }
               R.id.nav_thisMonth -> {
                   startActivity(Intent(this@DailyActivity, MonthlyActivity::class.java))
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
           FirebaseAuth.getInstance().signOut()
    }

    companion object {
           fun getLaunchIntent(from: Context) = Intent(from, DailyActivity::class.java).apply {
               addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
           }
       }

   }
