package com.example.diaryapplication

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_daily.*

import kotlinx.android.synthetic.main.activity_weekly.*
import kotlinx.android.synthetic.main.activity_weekly.drawerLayout
import kotlinx.android.synthetic.main.activity_weekly.navView
import kotlinx.android.synthetic.main.content_daily.*
import kotlinx.android.synthetic.main.content_weekly.*
import kotlinx.android.synthetic.main.content_weekly.toolbar
import kotlinx.coroutines.runBlocking

class WeeklyActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weekly)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        val dataSet = database.collection(CollectionName.MY_DIARY.name).get().result

        val dataList = dataSet!!.documents.map {
            DiaryData(date = it.get("date").toString(), detail = it.get("detail").toString())
         }

        viewManager = LinearLayoutManager(this)
        viewAdapter = MyAdapter(dataList)

        recyclerView = myRecyclerView.apply {

            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)

        menu!!.apply {
            findItem(R.id.menu_delete).isVisible = false
            findItem(R.id.menu_edit).isVisible = false
            findItem(R.id.menu_done).isVisible = false
            findItem(R.id.menu_photo).isVisible = false
        }
        return true
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_profile -> {
                Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_today -> {
                startActivity(Intent(this@WeeklyActivity, DailyActivity::class.java))
            }
            R.id.nav_thisWeek -> {

            }
            R.id.nav_thisMonth -> {
                startActivity(Intent(this@WeeklyActivity, MonthlyActivity::class.java))
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
