package com.satya.newyorktimes.presentation.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.satya.newyorktimes.R
import com.satya.newyorktimes.R.id.drawer_layout
import com.satya.newyorktimes.presentation.fragment.BaseFragment
import com.satya.newyorktimes.presentation.fragment.FragmentInteractionListener
import com.satya.newyorktimes.presentation.fragment.NewsListFragment
import com.satya.newyorktimes.util.inTransaction
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.app_bar_news.*

class NewsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    FragmentInteractionListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        setSupportActionBar(toolbar)


        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        supportFragmentManager.inTransaction {
            setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            ).replace(R.id.fragment_container, NewsListFragment.newInstance())

        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_share -> {

            }
            R.id.nav_about -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }




    override fun openFragment(fragment: BaseFragment) {
        supportFragmentManager.inTransaction {
            setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            ).add(R.id.fragment_container, fragment)
                .addToBackStack(fragment.javaClass.name)

        }
    }


}
