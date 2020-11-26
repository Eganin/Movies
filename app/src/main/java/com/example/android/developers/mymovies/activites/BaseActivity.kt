package com.example.android.developers.mymovies.activites

import android.content.Intent
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.android.developers.mymovies.R
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {
    open fun hideActionBar() {
        val actionBar = supportActionBar
        actionBar?.hide()
    }

    open fun showSnackBar(text: String , coordinatorLayout: CoordinatorLayout) {
        Snackbar.make(coordinatorLayout, text, Snackbar.LENGTH_LONG).show()
    }

    open fun clickedNavigationView(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_main -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.item_favourite -> {
                    startActivity(Intent(this, FavouriteActivity::class.java))
                    true
                }

                else -> false
            }
        }
    }


}