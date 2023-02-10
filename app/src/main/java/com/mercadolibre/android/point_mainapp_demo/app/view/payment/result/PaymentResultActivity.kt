package com.mercadolibre.android.point_mainapp_demo.app.view.payment.result

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mercadolibre.android.point_integration_sdk.nativesdk.MPManager
import com.mercadolibre.android.point_mainapp_demo.app.R
import com.mercadolibre.android.point_mainapp_demo.app.databinding.PointMainappDemoAppActivityPaymentResultBinding
import com.mercadolibre.android.point_mainapp_demo.app.view.home.HomeActivity

class PaymentResultActivity : AppCompatActivity() {

    private var binding: PointMainappDemoAppActivityPaymentResultBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PointMainappDemoAppActivityPaymentResultBinding.inflate(layoutInflater)
        binding?.run { setContentView(root) }

        configPaymentResult()
        configGotoHomeButton()
    }

    private fun configPaymentResult() {
        intent.data?.let { data ->
            val paymentFlowResult = MPManager.paymentFlow.parseResponse(data)
            binding?.run {
                if (paymentFlowResult.paymentStatusError.isEmpty()) {
                    val reference =
                        "${getString(R.string.point_mainapp_demo_app_lab_reference)}: ${paymentFlowResult.paymentReference}"
                    pointMainappDemoAppReferenceText.text = reference
                } else {
                    pointMainappDemoAppTextview.text = getString(R.string.point_mainapp_demo_app_payment_error)
                    with(pointMainappDemoAppImageview) {
                        setImageResource(R.drawable.point_mainapp_demo_app_ic_error)
                        setColorFilter(ContextCompat.getColor(context, android.R.color.holo_red_light), android.graphics.PorterDuff.Mode.SRC_IN);
                    }
                    val reference =
                        "${getString(R.string.point_mainapp_demo_app_lab_error_reference)}: ${paymentFlowResult.paymentStatusError}"
                    pointMainappDemoAppReferenceText.text = reference
                }
            }
        }
    }

    private fun configGotoHomeButton() {
        binding?.pointMainappDemoAppGoToHomeButton?.setOnClickListener { goToHome() }
    }

    private fun goToHome() {
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(homeIntent)
        finish()
    }
}
