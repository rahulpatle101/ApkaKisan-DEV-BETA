package com.apkakisan.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class VDDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        val tvCounter = findViewById<TextView>(R.id.tvCounter)
        tvCounter.text = "0"

        val vdDemoViewModel = VDDemoViewModel()

        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        btnSubmit.setOnClickListener {
            tvCounter.text = vdDemoViewModel.incrementCount(tvCounter.text.toString().trim())
        }
    }
}