package com.apkakisan.myapplication.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import android.content.Intent
import com.apkakisan.myapplication.registration.LoginActivity
import android.os.Build
import android.os.Handler
import android.util.Pair
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.apkakisan.myapplication.R
import com.apkakisan.myapplication.helpers.BuildTypeUtil
import com.apkakisan.myapplication.order.HomeActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //This Line will hide the status bar from the screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash)

        val image = findViewById<ImageView>(R.id.imageView)
        val tvAppName = findViewById<TextView>(R.id.tvAppName)

        val topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        image.animation = topAnim
        tvAppName.animation = topAnim

        Handler().postDelayed({ //Call next screen
            if (BuildTypeUtil.isDebug()) {
//                openLoginActivity(image)
                openHomeActivity()
            } else
                openLoginActivity(image)
        }, DELAY_SPLASH_SCREEN)
    }

    private fun openLoginActivity(image: ImageView?) {
        val intent = Intent(this@SplashActivity, LoginActivity::class.java)

        //Attach all the elements those you want to animate in design
        val pairs = arrayOfNulls<Pair<View, String>>(1)
        pairs[0] = Pair<View, String>(image, "logo_image")

        //wrap the call in API level 21 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //                val options =
            //                    ActivityOptions.makeSceneTransitionAnimation(this@SplashActivity, *pairs)
            //                startActivity(intent, options.toBundle())
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
        private const val DELAY_SPLASH_SCREEN = 3000L
    }
}