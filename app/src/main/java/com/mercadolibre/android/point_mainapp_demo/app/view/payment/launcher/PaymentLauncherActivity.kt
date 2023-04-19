package com.mercadolibre.android.point_mainapp_demo.app.view.payment.launcher

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mercadolibre.android.point_integration_sdk.nativesdk.MPManager
import com.mercadolibre.android.point_mainapp_demo.app.databinding.PointMainappDemoAppActivityPaymentLauncherBinding

/** Main activity class */
class PaymentLauncherActivity : AppCompatActivity() {

    private var binding: PointMainappDemoAppActivityPaymentLauncherBinding? = null
    private val paymentFlow = MPManager.paymentFlow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PointMainappDemoAppActivityPaymentLauncherBinding.inflate(layoutInflater)
        binding?.run { setContentView(root) }

        configPaymentButton()
    }

    private fun configPaymentButton() {
        binding?.sendPaymentActionButton?.setOnClickListener {
            val amount = binding?.amountEditText?.text?.toString()
            val description = binding?.descriptionEditText?.text?.toString()
            if (!amount.isNullOrEmpty()) {
                launchPaymentFlowIntent(
                    amount = amount,
                    description = description,
                    context = this
                )
            }
        }
    }

    private fun launchPaymentFlowIntent(
        amount: String,
        description: String?,
        context: Context,
    ) {
        val uriSuccess = paymentFlow.buildCallbackUri(
            "mercadopago://launcher_native_app",
            "callback_success",
            hashMapOf("attr" to "123"),
            "demo_app"
        )
        val uriError = paymentFlow.buildCallbackUri(
            "mercadopago://launcher_native_app",
            "callback_error",
            hashMapOf("attr" to "456"),
            "demo_app"
        )
        paymentFlow.launchPaymentFlowActivity(amount, description, uriSuccess, uriError, context)
    }
}
