package com.apkakisan.myapplication.common

import android.os.Bundle
import com.apkakisan.myapplication.BaseActivity
import com.apkakisan.myapplication.R
import com.apkakisan.myapplication.databinding.ActivityTermsPrivacyBinding
import com.apkakisan.myapplication.utils.AssetUtil
import java.io.IOException

class TermsPrivacyActivity : BaseActivity() {

    private lateinit var binding: ActivityTermsPrivacyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideStatusBar()
        binding = ActivityTermsPrivacyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            binding.tvText.text = AssetUtil.readFile(this, AssetUtil.FILENAME_TERMS_PRIVACY)
        } catch (ex: IOException) {
            binding.tvText.text = getString(R.string.file_load_failed)
        }
    }

    companion object {
        private const val TAG = "TermsPrivacyActivity"
    }
}