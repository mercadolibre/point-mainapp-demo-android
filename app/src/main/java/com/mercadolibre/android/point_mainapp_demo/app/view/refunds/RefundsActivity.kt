package com.mercadolibre.android.point_mainapp_demo.app.view.refunds

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mercadolibre.android.point_mainapp_demo.app.databinding.PointMainappDemoAppActivityRefundsBinding

class RefundsActivity : AppCompatActivity() {

    private var binding: PointMainappDemoAppActivityRefundsBinding? = null

    private val viewModel by viewModels<RefundsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PointMainappDemoAppActivityRefundsBinding.inflate(layoutInflater)
        binding?.run { setContentView(root) }

        configRefundsButton()
    }

    private fun configRefundsButton() {
        binding?.sendRefundActionButton?.setOnClickListener {
            val amount = binding?.amountEditText?.text?.toString()
            val accessToken = binding?.accessTokenEditText?.text?.toString()
            val paymentId = binding?.paymentIdEditText?.text?.toString()
            viewModel.performRefund(paymentId?.toLong() ?: 0L, amount?.toDouble() ?: 0.0, accessToken ?: "")
        }
    }
}
