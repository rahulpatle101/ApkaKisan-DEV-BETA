package com.apkakisan.myapplication

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.apkakisan.myapplication.contracts.UIInterface
import com.apkakisan.myapplication.helpers.showShortToast
import com.apkakisan.myapplication.helpers.somethingWentWrong

abstract class BaseActivity : AppCompatActivity(), UIInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window?.statusBarColor = ContextCompat.getColor(this, R.color.white)
    }

    override fun showLoadingView() {}

    override fun showContentView() {}

    override fun showEmptyView() {
        showShortToast(getString(R.string.no_result_found))
    }

    override fun showErrorView() {
        somethingWentWrong()
    }
}