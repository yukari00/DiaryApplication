package com.example.diaryapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.activity_monthly.*
import kotlinx.android.synthetic.main.content_monthly.*
import java.text.SimpleDateFormat
import java.util.*

class MonthlyActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)

        menu!!.apply {
            findItem(R.id.menu_delete).isVisible = false
            findItem(R.id.menu_edit).isVisible = false
            findItem(R.id.menu_done).isVisible = false
        }
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_profile -> {
                Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_today -> {
                startActivity(Intent(this@MonthlyActivity, DailyActivity::class.java))
            }
            R.id.nav_thisWeek -> {
                startActivity(Intent(this@MonthlyActivity, WeeklyActivity::class.java))
            }
            R.id.nav_thisMonth -> {

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

}
