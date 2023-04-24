package com.androidvibes.mindbodytest.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import com.androidvibes.mindbodytest.MindBodyApp
import com.androidvibes.mindbodytest.R
import com.androidvibes.mindbodytest.ui.fragments.CountryListFragment
import com.androidvibes.mindbodytest.ui.fragments.ProvincesListFragment

fun isInternetAvailable(context: Context): Boolean {
    var result = false
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }

            }
        }
    }

    return result
}

fun AppCompatActivity.addCountryListFragment(@IdRes containerId: Int) {
    supportFragmentManager.beginTransaction().add(
        containerId,
        CountryListFragment(),
        CountryListFragment::class.java.simpleName
    ).commit()
}

fun AppCompatActivity.addProvincesListFragmentFragment(@IdRes containerId: Int) {
    supportFragmentManager.beginTransaction().add(
        containerId,
        ProvincesListFragment(),
        ProvincesListFragment::class.java.simpleName
    ).commit()
}

fun AppCompatActivity.removeProvincesListFragmentFragment() {

    val fragment = supportFragmentManager
        .findFragmentByTag(ProvincesListFragment::class.java.simpleName) ?: return
    supportFragmentManager
        .beginTransaction()
        .remove(fragment)
        .commit()

    addCountryListFragment(R.id.fragment_container)
}