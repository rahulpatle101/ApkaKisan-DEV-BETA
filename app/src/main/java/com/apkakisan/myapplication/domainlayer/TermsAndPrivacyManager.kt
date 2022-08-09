package com.apkakisan.myapplication.domainlayer

import android.content.Context
import android.content.Intent
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import com.apkakisan.myapplication.R
import com.apkakisan.myapplication.common.TermsPrivacyActivity
import com.apkakisan.myapplication.utils.StringUtil

class TermsAndPrivacyManager {

    fun formatTermsAndPolicyString(
        context: Context,
        textView: TextView
    ) {
        try {
            val spannableString = SpannableString(context.getString(R.string.terms_privacy))
            formatTermsOfUseString(context, spannableString)
            formatPrivacyPolicyString(context, spannableString)
            textView.text = spannableString
            textView.movementMethod = LinkMovementMethod.getInstance()
        } catch (ex: Exception) {
            textView.text = context.getString(R.string.terms_privacy)
        }
    }

    @Throws(Exception::class)
    private fun formatTermsOfUseString(
        context: Context, spannableString: SpannableString
    ) {
        val startIndex = StringUtil.findStartIndex(
            context.getString(R.string.terms_privacy),
            context.getString(R.string.terms_of_use)
        )
        val endIndex = StringUtil.findEndIndex(
            startIndex,
            context.getString(R.string.terms_of_use)
        )
        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                widget.invalidate()
                context.startActivity(Intent(context, TermsPrivacyActivity::class.java))
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    @Throws(Exception::class)
    private fun formatPrivacyPolicyString(
        context: Context,
        spannableString: SpannableString
    ) {
        val startIndex = StringUtil.findStartIndex(
            context.getString(R.string.terms_privacy),
            context.getString(R.string.privacy_policy)
        )
        val endIndex = StringUtil.findEndIndex(
            startIndex,
            context.getString(R.string.privacy_policy)
        )
        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                widget.invalidate()
                context.startActivity(Intent(context, TermsPrivacyActivity::class.java))
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}