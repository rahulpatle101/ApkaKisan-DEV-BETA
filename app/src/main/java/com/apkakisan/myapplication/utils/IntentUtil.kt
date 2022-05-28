package com.apkakisan.myapplication.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Patterns
import android.webkit.URLUtil
import androidx.appcompat.app.AppCompatActivity
import com.apkakisan.myapplication.BuildConfig

object IntentUtil {
    fun makeCall(activity: Activity, number: String) {
        val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
        activity.startActivity(callIntent)
    }

    fun openLinkedIn(activity: AppCompatActivity, link: String) {
        val i = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        activity.startActivity(i)
    }

    fun openFacebook(activity: AppCompatActivity, link: String, linkEnd: String) {
        val facebookUrl = link
        val facebookName = linkEnd
        try {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=$facebookUrl"))
            activity.startActivity(intent)
        } catch (e: Exception) {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://www.facebook.com/$facebookName")
                )
            )
        }
    }

    fun openYouTube(activity: AppCompatActivity, link: String, linkEnd: String) {
        try {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse(link))
            activity.startActivity(intent)
        } catch (e: Exception) {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(link)
                )
            )
        }
    }

    fun openInsta(activity: AppCompatActivity, link: String, linkEnd: String) {
        val instagramUrl = link
        val instagramUser = linkEnd
        try {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/$instagramUser"))
            activity.startActivity(intent)
        } catch (e: Exception) {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(instagramUrl)
                )
            )
        }
    }

    fun openTwitter(activity: AppCompatActivity, link: String, linkEnd: String) {
        val twitterUrl = link
        val twitterId = linkEnd
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=$twitterId"))
            activity.startActivity(intent)
        } catch (e: Exception) {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(twitterUrl)
                )
            )
        }
    }

    fun openWebsite(activity: AppCompatActivity, link: String) {
        if (URLUtil.isValidUrl(link) && Patterns.WEB_URL.matcher(link).matches()) {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            activity.startActivity(i)
        }
    }

    fun openPlayStore(activity: AppCompatActivity) {
        try {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}")
                )
            )
        } catch (anfe: ActivityNotFoundException) {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")
                )
            )
        }
    }
}