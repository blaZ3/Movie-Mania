package com.example.moviemania.app.base

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dailytools.healthbuddy.base.BaseView

abstract class BaseFragment : Fragment(), BaseView {

    fun showToast(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

}