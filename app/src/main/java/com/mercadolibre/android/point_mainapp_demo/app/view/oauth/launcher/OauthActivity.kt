package com.mercadolibre.android.point_mainapp_demo.app.view.oauth.launcher

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mercadolibre.android.point_mainapp_demo.app.databinding.ActivityOauthBinding

class OauthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOauthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOauthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configOauthButton()
    }

    private fun configOauthButton() {
        binding.apply {
            sendOauthActionButton.setOnClickListener {
                val redirectUrl = redirectUrlEditText.text.toString()
                if (redirectUrl.isNotEmpty()) {
                    val intent = launchOauthFlowIntent(redirectUrl)
                    startActivity(intent)
                }
            }
        }
    }

    private fun launchOauthFlowIntent(redirect: String): Intent = TODO()
}
