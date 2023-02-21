package com.mercadolibre.android.point_mainapp_demo.app.view.oauth.launcher

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mercadolibre.android.point_integration_sdk.nativesdk.MPManager
import com.mercadolibre.android.point_mainapp_demo.app.R
import com.mercadolibre.android.point_mainapp_demo.app.databinding.ActivityOauthBinding

class OauthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOauthBinding
    private val oauth = MPManager.oauth

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
                    launchOauthFlow(redirectUrl)
                } else {
                    setLayoutError()
                }
            }
        }
    }

    private fun setLayoutError() {
        binding.redirectUrlInputLayout.apply {
            boxStrokeColor = getColor(R.color.errorColor)
            hintTextColor = ColorStateList(
                arrayOf(intArrayOf(getColor(R.color.errorColor))),
                intArrayOf(getColor(R.color.errorColor))
            )
            error = MESSAGE_EMPTY_PARAMETER
            setStartIconTintList(
                ColorStateList(
                    arrayOf(intArrayOf(getColor(R.color.errorColor))),
                    intArrayOf(getColor(R.color.errorColor))
                )
            )
            listenerIconError()
        }
    }

    private fun listenerIconError() {
        binding.redirectUrlInputLayout.apply {
            setErrorIconOnClickListener {
                isErrorEnabled = false
                setStartIconTintList(
                    ColorStateList(
                        arrayOf(intArrayOf(getColor(R.color.primaryTextColor))),
                        intArrayOf(getColor(R.color.primaryTextColor))
                    )
                )
                boxStrokeColor = getColor(R.color.primaryColor)
                hintTextColor = ColorStateList(
                    arrayOf(intArrayOf(getColor(R.color.primaryColor))),
                    intArrayOf(getColor(R.color.primaryColor))
                )
            }
        }
    }

    private fun launchOauthFlow(redirect: String) =
        oauth.launchOauthIntent(redirect)?.let {
            startActivity(it)
            finish()
        } ?: throw IllegalArgumentException(MESSAGE_PARAM_INVALID)

    companion object {
        private const val MESSAGE_PARAM_INVALID = "ParamError: param client id or redirect url invalid."
        private const val MESSAGE_EMPTY_PARAMETER = "Empty url parameter"
    }
}
