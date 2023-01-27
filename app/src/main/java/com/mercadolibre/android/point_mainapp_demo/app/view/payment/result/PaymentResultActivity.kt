package com.mercadolibre.android.point_mainapp_demo.app.view.payment.result

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.mercadolibre.android.point_mainapp_demo.app.R
import com.mercadolibre.android.point_mainapp_demo.app.data.PaymentSuccessResponse
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
            val queryPaymentSuccess = data.getQueryParameter("payment_success")
            queryPaymentSuccess?.let {
                val paymentResult = Gson().fromJson(it, PaymentSuccessResponse::class.java)
                binding?.run {
                    val reference = "${getString(R.string.point_mainapp_demo_app_lab_reference)}: ${paymentResult.paymentReference}"
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
