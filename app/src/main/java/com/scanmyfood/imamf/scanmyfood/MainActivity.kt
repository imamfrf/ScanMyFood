package com.scanmyfood.imamf.scanmyfood

import android.content.Intent
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.scanmyfood.imamf.scanmyfood.BeliFragment.BeliFragment
import com.scanmyfood.imamf.scanmyfood.HomeFragment.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var btmNav: BottomNavigationView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation!!.setOnNavigationItemSelectedListener { item ->
            var fragment: Fragment? = null
            when (item.itemId) {
                R.id.navigation_home -> {
                    fragment = HomeFragment()
                    loadFragment(fragment)
                    // navigation.selectedItemId = R.id.navigation_home

                }
                R.id.navigation_recommend -> {
                    fragment = BeliFragment()
                    loadFragment(fragment)
                    //navigation.selectedItemId = R.id.navigation_recommend
                }
                R.id.navigation_profile -> {
                    fragment = ProfileFragment()
                    loadFragment(fragment)
                }
            }
            return@setOnNavigationItemSelectedListener true
        }

//        //btm nav
//        btmNav = findViewById(R.id.navigation)
//        btmNav!!.setOnNavigationItemSelectedListener(this)
        loadFragment(HomeFragment())
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFrg()).commit();



    }
//    private val onNavigationItemSelectedListener
//            = BottomNavigationView.OnNavigationItemSelectedListener { item ->
//
//        var fragment: Fragment? = null
//        when (item.itemId) {
//            R.id.navigation_home -> {
//                fragment = HomeFragment()
//                loadFragment(fragment)
//               // navigation.selectedItemId = R.id.navigation_home
//
//            }
//            R.id.navigation_recommend -> {
//                fragment = BeliFragment()
//                loadFragment(fragment)
//                //navigation.selectedItemId = R.id.navigation_recommend
//            }
//        }
//        false
//    }



    fun loadFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
        return true
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        fragment!!.onActivityResult(requestCode, resultCode, data)
    }


}

