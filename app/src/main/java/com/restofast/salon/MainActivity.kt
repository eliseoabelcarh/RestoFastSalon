package com.restofast.salon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var nav_view_main_activity : NavigationView
    private lateinit var drawer_layout_main_activity : DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nav_view_main_activity  = findViewById(R.id.nav_view_main_activity)
        drawer_layout_main_activity  = findViewById(R.id.drawer_layout_main_activity)

        navController = Navigation.findNavController(this,R.id.nav_host_fragment)
        nav_view_main_activity.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout_main_activity)



    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawer_layout_main_activity)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.drawer_menu, menu)
        return false
    }

    //ELIMINA OVERFLOW ACTION ITEMS MENU (3 PUNTITOS DEL ACTIONBAR)
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return false
    }

    override fun onBackPressed() {
        if (drawer_layout_main_activity.isDrawerOpen(GravityCompat.START)) {
            drawer_layout_main_activity.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


}