package com.apkakisan.myapplication.helpers

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.apkakisan.myapplication.R

object DialogUtil {

    fun alert(
        context: Context,
        message: String,
        onPositive: () -> Unit? = {}
    ) {
        AlertDialog.Builder(context).apply {
            if (message.isNotEmpty())
                setMessage(message)
            setCancelable(false)
            setPositiveButton(context.getString(R.string.ok)) { _, _ -> onPositive() }
            show()
        }
    }

    fun confirmationAlert(
        context: Context,
        title: String,
        message: String,
        yesBtnText: String,
        noBtnText: String,
        onPositive: () -> Unit = {},
        onNegative: () -> Unit = {}
    ) {
        AlertDialog.Builder(context).apply {
            if (title.isNotEmpty())
                setTitle(title)
            if (message.isNotEmpty())
                setMessage(message)
            setCancelable(false)
            setPositiveButton(yesBtnText) { _, _ -> onPositive() }
            setNegativeButton(noBtnText) { _, _ -> onNegative() }
            show()
        }
    }
}