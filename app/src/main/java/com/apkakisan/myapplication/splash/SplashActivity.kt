package com.apkakisan.myapplication.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.content.Intent
import com.apkakisan.myapplication.registration.LoginActivity
import android.os.Build
import android.os.Handler
import android.util.Pair
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.apkakisan.myapplication.R
import com.apkakisan.myapplication.helpers.LocalStore
import com.apkakisan.myapplication.utils.BuildTypeUtil
import com.apkakisan.myapplication.order.HomeActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window?.statusBarColor = ContextCompat.getColor(this, R.color.white)
        setContentView(R.layout.activity_splash)

        val image = findViewById<ImageView>(R.id.imageView)
        val tvAppName = findViewById<TextView>(R.id.tvAppName)

        val topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        image.animation = topAnim
        tvAppName.animation = topAnim

        Handler().postDelayed({
            LocalStore.getUser()?.let {
                openHomeActivity()
            } ?: openLoginActivity(image)
        }, DELAY_SPLASH_SCREEN)
    }

    private fun openLoginActivity(image: ImageView?) {
        val intent = Intent(this@SplashActivity, LoginActivity::class.java)

        //Attach all the elements those you want to animate in design
        val pairs = arrayOfNulls<Pair<View, String>>(1)
        pairs[0] = Pair<View, String>(image, "logo_image")

        //wrap the call in API level 21 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent)
        } else {
            startActivity(intent)
        }
        finish()
    }

    private fun openHomeActivity() {
        val intent = Intent(this@SplashActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private val DELAY_SPLASH_SCREEN =
            if (BuildTypeUtil.isDebug() || BuildTypeUtil.isDebugWithRegistration()) 1000L else 3000L
    }
}