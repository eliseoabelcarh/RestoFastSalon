package com.restofast.salon.orderTaking.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.restofast.salon.MainActivity
import com.restofast.salon.R
import com.restofast.salon.authentication.usecases.CloseSessionUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ActivityOrderTaking : AppCompatActivity() {

    @Inject
    lateinit var closeSessionUseCase: CloseSessionUseCase
    private lateinit var navController: NavController
    private lateinit var nav_view_activity_order_taking : NavigationView
    private lateinit var drawer_layout_activity_order_taking : DrawerLayout
    private lateinit var bottomNavView : BottomNavigationView
    private lateinit var navHostFragment : NavHostFragment

    /* EJEMPLO
         @Inject
         lateinit var userDao: UserDao
     */
    //ELIMINA OVERFLOW ACTION ITEMS MENU (3 PUNTITOS DEL ACTIONBAR)
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_taking)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_order_taking) as NavHostFragment
        bottomNavView = findViewById(R.id.bottom_navigation_orderTaking)
        NavigationUI.setupWithNavController(bottomNavView, navHostFragment.navController)

        nav_view_activity_order_taking  = findViewById(R.id.nav_view_activity_order_taking)
        drawer_layout_activity_order_taking  = findViewById(R.id.drawer_layout_activity_order_taking)

        navController = Navigation.findNavController(this,R.id.nav_host_fragment_order_taking)
        nav_view_activity_order_taking.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout_activity_order_taking)

        // MenuItem CERRAR SESION
        nav_view_activity_order_taking.menu.findItem(R.id.menuItemSignOutApp).setOnMenuItemClickListener{
            CoroutineScope(Dispatchers.Default).launch {
                closeSessionUseCase()
                redirectToLogin()
            }
            true
        }

    }


    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawer_layout_activity_order_taking)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.drawer_menu_order_taking, menu)
        return false
    }

    override fun onBackPressed() {
        if (drawer_layout_activity_order_taking.isDrawerOpen(GravityCompat.START)) {
            drawer_layout_activity_order_taking.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    private suspend fun redirectToLogin() = coroutineScope {
        startActivity(Intent(this@ActivityOrderTaking, MainActivity::class.java))
        finish()
    }

}