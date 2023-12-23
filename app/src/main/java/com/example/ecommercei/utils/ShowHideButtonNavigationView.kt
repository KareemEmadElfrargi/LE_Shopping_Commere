package com.example.ecommercei.utils

import android.view.View
import androidx.fragment.app.Fragment
import com.example.ecommercei.R
import com.example.ecommercei.activitys.ShoppingActivity
import com.example.ecommercei.databinding.FragmentDetailsProductsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.hideButtonNavigationView(){
    val bottonNavigationView =
        (activity as ShoppingActivity).findViewById<BottomNavigationView>(R.id.bottomNavigation)
    bottonNavigationView.visibility = View.GONE
}

fun Fragment.showButtonNavigationView(){
    val bottonNavigationView =
        (activity as ShoppingActivity).findViewById<BottomNavigationView>(R.id.bottomNavigation)
    bottonNavigationView.visibility = View.VISIBLE
}