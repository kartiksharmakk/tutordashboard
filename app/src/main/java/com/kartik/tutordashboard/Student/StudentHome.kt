package com.kartik.tutordashboard.Student

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.github.kwasow.bottomnavigationcircles.BottomNavigationCircles
import com.google.android.material.navigation.NavigationView
import com.kartik.tutordashboard.Data.Prefs
import com.kartik.tutordashboard.R
import com.kartik.tutordashboard.databinding.ActivityStudentHomeBinding


class StudentHome : AppCompatActivity() {
    lateinit var binding: ActivityStudentHomeBinding
    lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(com.kartik.tutordashboard.R.id.main_nav_host) //Initialising navController

        appBarConfiguration = AppBarConfiguration.Builder(
            com.kartik.tutordashboard.R.id.studentHomeFragment, com.kartik.tutordashboard.R.id.studentProfileFragment,
        ) //Pass the ids of fragments from nav_graph which you d'ont want to show back button in toolbar
            .setOpenableLayout(binding.mainDrawerLayout) //Pass the drawer layout id from activity xml
            .build()

        setSupportActionBar(binding.mainToolbar) //Set toolbar

        setupActionBarWithNavController(navController, appBarConfiguration) //Setup toolbar with back button and drawer icon according to appBarConfiguration


        val bottomNavBar = findViewById<BottomNavigationCircles>(com.kartik.tutordashboard.R.id.main_bottom_navigation_view)
        bottomNavBar.setOnNavigationItemSelectedListener {
            when(it.itemId){
                com.kartik.tutordashboard.R.id.studentHomeFragment->{
                    navController.navigate(com.kartik.tutordashboard.R.id.studentHomeFragment)
                    true
                }
                com.kartik.tutordashboard.R.id.studentTestListFragment->{
                    navController.navigate(com.kartik.tutordashboard.R.id.studentTestListFragment)
                    true
                }
                com.kartik.tutordashboard.R.id.studentProfileFragment->{
                    navController.navigate(com.kartik.tutordashboard.R.id.studentProfileFragment)
                    true
                }
                else->{
                    false
                }
            }
        }


        val DrawerNavBar = findViewById<NavigationView>(com.kartik.tutordashboard.R.id.main_navigation_view)

        DrawerNavBar.setNavigationItemSelectedListener { it: MenuItem ->
            when (it.itemId) {
                R.id.nav_gpa_calculator ->{
                    navController.navigate(R.id.nav_gpa_calculator)
                    binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_notes ->{
                    navController.navigate(R.id.nav_notes)
                    binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_assignment ->{
                    navController.navigate(R.id.nav_assignment)
                    binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> {
                    true
                }
            }
        }

        val userName = Prefs.getUsername(this)
        val email = Prefs.getUSerEmailEncoded(this)

        val navDrawer = DrawerNavBar.getHeaderView(0)

        val userNameDrawer = navDrawer.findViewById<TextView>(R.id.userNameDrawer)
        userNameDrawer.text = userName

        val userEmailDrawer = navDrawer.findViewById<TextView>(R.id.userEmailDrawer)
        userEmailDrawer.text = email?.replace("(dot)", ".")



       visibilityNavElements(navController) //If you want to hide drawer or bottom navigation configure that in this function
    }

    private fun visibilityNavElements(navController: NavController) {

        //Listen for the change in fragment (navigation) and hide or show drawer or bottom navigation accordingly if required
        //Modify this according to your need
        //If you want you can implement logic to deselect(styling) the bottom navigation menu item when drawer item selected using listener

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.studentHomeFragment -> showBothNavigation()
       //         R.id.settingsFragment -> hideBottomNavigation()
                else -> showBothNavigation()
            }
        }

    }

    fun hideBothNavigation() { //Hide both drawer and bottom navigation bar
        binding.mainBottomNavigationView.visibility = View.GONE
        binding.mainNavigationView.visibility = View.GONE
        binding.mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED) //To lock navigation drawer so that it don't respond to swipe gesture
    }

    fun hideBottomNavigation() { //Hide bottom navigation
        binding.mainBottomNavigationView.visibility = View.GONE
        binding.mainNavigationView.visibility = View.VISIBLE
        binding.mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED) //To unlock navigation drawer

        binding.mainNavigationView.setupWithNavController(navController) //Setup Drawer navigation with navController
    }


    fun showBottomNavigation() { //Hide bottom navigation
        binding.mainBottomNavigationView.visibility = View.VISIBLE
        binding.mainNavigationView.visibility = View.GONE
        binding.mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED) //To unlock navigation drawer

        binding.mainBottomNavigationView.setupWithNavController(navController) //Setup Drawer navigation with navController
    }
    private fun showBothNavigation() {
        binding.mainBottomNavigationView.visibility = View.VISIBLE
        binding.mainNavigationView.visibility = View.VISIBLE
        binding.mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        setupNavControl() //To configure navController with drawer and bottom navigation
    }

    private fun setupNavControl() {
        binding.mainNavigationView.setupWithNavController(navController) //Setup Drawer navigation with navController
        binding.mainBottomNavigationView.setupWithNavController(navController) //Setup Bottom navigation with navController
    }

    fun exitApp() { //To exit the application call this function from fragment
        this.finishAffinity()
    }

    override fun onSupportNavigateUp(): Boolean { //Setup appBarConfiguration for back arrow
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    override fun onBackPressed() {
        when { //If drawer layout is open close that on back pressed
            binding.mainDrawerLayout.isDrawerOpen(GravityCompat.START) -> {
                binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
            }
            else -> {
                super.onBackPressed() //If drawer is already in closed condition then go back
            }
        }
    }
}