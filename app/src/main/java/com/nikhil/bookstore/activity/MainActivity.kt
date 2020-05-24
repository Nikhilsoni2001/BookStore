package com.nikhil.bookstore.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.nikhil.bookstore.R
import com.nikhil.bookstore.fragment.AboutFragment
import com.nikhil.bookstore.fragment.DashboardFragment
import com.nikhil.bookstore.fragment.FavouriteFragment
import com.nikhil.bookstore.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var coordinatorLayout: CoordinatorLayout
    private lateinit var toolbar: Toolbar
    private lateinit var frameLayout: FrameLayout
    private lateinit var navigationView: NavigationView

    var previousMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        coordinatorLayout = findViewById(R.id.coordinator_layout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frame_layout)
        navigationView = findViewById(R.id.navigationView)
        setUpToolbar()

        openDashboard()     //set dashboard as opening window

        val actionBarDrawerToggle = ActionBarDrawerToggle(      //defining hamburger
            this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        navigationView.setNavigationItemSelectedListener {

            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = false
            }
            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it

            when (it.itemId) {
                R.id.dashboard -> {
                    openDashboard()
                    drawerLayout.closeDrawers()
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout,
                            ProfileFragment()
                        )
                        .commit()

                    supportActionBar?.title = "Profile"
                    drawerLayout.closeDrawers()
                }
                R.id.favorite -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout,
                            FavouriteFragment()
                        )
                        .commit()

                    supportActionBar?.title = "Favourite"
                    drawerLayout.closeDrawers()
                }
                R.id.about -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout,
                            AboutFragment()
                        )
                        .commit()

                    supportActionBar?.title = "About"
                    drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }

        drawerLayout.addDrawerListener(actionBarDrawerToggle)   //this will make hamburger
        actionBarDrawerToggle.syncState()               //change hamburger icon to back and vice-versa
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Title toolbar"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)        //making hamburger working
        }

        return super.onOptionsItemSelected(item)
    }

    fun openDashboard() {
        val fragment = DashboardFragment()
        val transanction = supportFragmentManager.beginTransaction()
        transanction.replace(R.id.frame_layout, fragment)
        transanction.addToBackStack("Dashboard")
        transanction.commit()
        supportActionBar?.title = "Dashboard"
        navigationView.setCheckedItem(
            R.id.dashboard
        )
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame_layout)
        when (frag) {
            !is DashboardFragment -> openDashboard()
            else -> super.onBackPressed()
        }
    }
}
