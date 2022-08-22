package com.apkakisan.myapplication.common

import android.os.Bundle
import com.apkakisan.myapplication.BaseActivity
import com.apkakisan.myapplication.R
import com.apkakisan.myapplication.databinding.ActivityTermsPrivacyBinding
import com.apkakisan.myapplication.utils.AssetUtil
import java.io.IOException

enum class ContentType {
    TERMS_OF_USE,
    PRIVACY_POLICY
}

class TermsPrivacyActivity : BaseActivity() {

    private lateinit var binding: ActivityTermsPrivacyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideStatusBar()
        binding = ActivityTermsPrivacyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val contentType: ContentType = intent?.getSerializableExtra(CONTENT_TYPE) as ContentType

        try {
            binding.tvText.text = AssetUtil.readFile(
                this,
                if (contentType == ContentType.TERMS_OF_USE)
                    AssetUtil.FILENAME_TERMS_OF_USE
                else AssetUtil.FILENAME_PRIVACY_POLICY
            )
        } catch (ex: IOException) {
            binding.tvText.text = getString(R.string.file_load_failed)
        }
    }

    companion object {
        private const val TAG = "TermsPrivacyActivity"

        const val CONTENT_TYPE = "content_type"
    }
}