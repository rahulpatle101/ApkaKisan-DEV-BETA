package com.apkakisan.myapplication.helpers

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import com.apkakisan.myapplication.R

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Context.showShortToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.showLongToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

fun Fragment.showShortToast(msg: String) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}

fun Fragment.showLongToast(msg: String) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
}

fun Activity.somethingWentWrong() {
    showShortToast(getString(R.string.something_went_wrong))
}

fun Fragment.somethingWentWrong() {
    showShortToast(getString(R.string.something_went_wrong))
}

fun Activity.comingSoon() {
    showShortToast(getString(R.string.coming_soon))
}

fun Fragment.comingSoon() {
    showShortToast(getString(R.string.coming_soon))
}

fun AppCompatActivity.replaceFragment(
    fragment: Fragment,
    isAddBackToStack: Boolean,
    TAG: String
) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.replace(R.id.container_fragment, fragment, TAG)
    if (isAddBackToStack)
        transaction.addToBackStack(TAG)
    transaction.commit()
}

fun AppCompatActivity.addFragment(
    fragment: Fragment,
    isAddBackToStack: Boolean,
    TAG: String
) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.add(R.id.container_fragment, fragment, TAG)
    if (isAddBackToStack)
        transaction.addToBackStack(TAG)
    transaction.commit()
}

fun Fragment.replaceFragment(
    fragment: Fragment,
    isAddBackToStack: Boolean,
    TAG: String
) {
    val transaction = activity?.supportFragmentManager?.beginTransaction()
    transaction?.replace(R.id.container_fragment, fragment, TAG)
    if (isAddBackToStack)
        transaction?.addToBackStack(TAG)
    transaction?.commit()
}

fun Fragment.addFragment(
    fragment: Fragment,
    isAddBackToStack: Boolean,
    TAG: String
) {
    val transaction = activity?.supportFragmentManager?.beginTransaction()
    transaction?.add(R.id.container_fragment, fragment, TAG)
    if (isAddBackToStack)
        transaction?.addToBackStack(TAG)
    transaction?.commit()
}

fun Fragment.replaceChildFragment(
    fragment: Fragment,
    isAddBackToStack: Boolean,
    TAG: String
) {
    val transaction = childFragmentManager.beginTransaction()
    transaction.replace(R.id.container_fragment, fragment, TAG)
    if (isAddBackToStack)
        transaction.addToBackStack(TAG)
    transaction.commit()
}

fun Fragment.addChildFragment(
    fragment: Fragment,
    isAddBackToStack: Boolean,
    TAG: String
) {
    val transaction = childFragmentManager.beginTransaction()
    transaction.add(R.id.container_fragment, fragment, TAG)
    if (isAddBackToStack)
        transaction.addToBackStack(TAG)
    transaction.commit()
}

fun Fragment.popFragment() {
    activity?.supportFragmentManager?.popBackStack()
}