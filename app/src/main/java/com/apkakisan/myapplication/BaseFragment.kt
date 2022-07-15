package com.apkakisan.myapplication

import androidx.fragment.app.Fragment
import com.apkakisan.myapplication.contracts.UIInterface
import com.apkakisan.myapplication.helpers.showShortToast
import com.apkakisan.myapplication.helpers.somethingWentWrong

abstract class BaseFragment : Fragment(), UIInterface {

    override fun showLoadingView() {}
    override fun showContentView() {}
    override fun showEmptyView() {
        showShortToast(getString(R.string.no_result_found))
    }

    override fun showErrorView() {
        somethingWentWrong()
    }
}