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
        binding?.apply {
            sendPaymentActionButton.setOnClickListener {
                val amount = binding?.amountEditText?.text?.toString()
                val description = binding?.descriptionEditText?.text?.toString()
                if (!amount.isNullOrEmpty()) {
                    launchPaymentFlowIntent(
                        amount = amount,
                        description = description,
                        context = this@PaymentLauncherActivity
                    )
                }
            }
            pointMainappDemoBackArrow.setOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun launchPaymentFlowIntent(
        amount: String,
        description: String?,
        context: Context,
    ) {
        val uriSuccess = paymentFlow.buildCallbackUri(
            "mercadopago://smart_integrations/payment_result",
            "success",
            hashMapOf("message" to "testSuccess"),
            "demo.app"
        )
        val uriError = paymentFlow.buildCallbackUri(
            "mercadopago://smart_integrations/payment_result",
            "error",
            hashMapOf("message" to "testError"),
            "demo.app"
        )
        paymentFlow.launchPaymentFlowActivity(amount, description, uriSuccess, uriError, context)
    }
}
