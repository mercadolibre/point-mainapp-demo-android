package com.mercadolibre.android.point_mainapp_demo.app.view.payment.launcher

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mercadolibre.android.point_integration_sdk.nativesdk.payment.PaymentFlow
import com.mercadolibre.android.point_mainapp_demo.app.databinding.PointMainappDemoAppActivityPaymentLauncherBinding

/** Main activity class */
class PaymentLauncherActivity : AppCompatActivity() {

    private var binding: PointMainappDemoAppActivityPaymentLauncherBinding? = null
    private val paymentFlow = PaymentFlow()

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
            if (!amount.isNullOrEmpty() && !description.isNullOrEmpty()) {
                val intent = launchPaymentFlowIntent(
                    amount = amount,
                    description = description,
                )
                startActivity(intent)
            }
        }
    }

    private fun launchPaymentFlowIntent(
        amount: String,
        description: String
    ): Intent {
        val uriSuccess = paymentFlow.buildUri("https://success", "callback_success", "{\"attr\": \"123\"}", "demo_app")
        val uriError = paymentFlow.buildUri("https://error", "callback_error", "{\"attr\": \"345\"}", "demo_app")
        return paymentFlow.launchPaymentFlowIntent(amount, description, uriSuccess, uriError, "123456")
    }
}
