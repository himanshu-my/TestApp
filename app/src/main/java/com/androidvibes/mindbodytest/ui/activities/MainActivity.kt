package com.androidvibes.mindbodytest.ui.activities

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.androidvibes.mindbodytest.R
import com.androidvibes.mindbodytest.databinding.ActivityMainBinding
import com.androidvibes.mindbodytest.utils.addCountryListFragment
import com.androidvibes.mindbodytest.utils.removeProvincesListFragmentFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var mBinding: ActivityMainBinding? = null
    var selectedCountryCode: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mBinding?.backIcon?.setOnClickListener {
            removeProvincesListFragmentFragment()
        }

        if (savedInstanceState != null) {
            selectedCountryCode = savedInstanceState.getString("Country Code", "") ?: ""
        } else {
            addCountryListFragment(R.id.fragment_container)
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("Country Code", selectedCountryCode)
        super.onSaveInstanceState(outState)
    }


    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }

    fun showHideBackButton(makeVisible: Boolean) {
        mBinding?.backIcon?.visibility = if (makeVisible) View.VISIBLE else View.GONE
    }

    fun setScreenName(screenName: String) {
        mBinding?.screenName?.text = screenName
    }

}