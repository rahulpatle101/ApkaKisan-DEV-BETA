package com.apkakisan.myapplication

import androidx.fragment.app.Fragment
import com.apkakisan.myapplication.helpers.showShortToast

open class BaseFragment : Fragment() {
    protected fun somethingWentWrong() {
        showShortToast(getString(R.string.something_went_wrong))
    }
}