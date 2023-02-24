package com.mercadolibre.android.point_mainapp_demo.app.view.refunds

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mercadolibre.android.point_mainapp_demo.app.R
import com.mercadolibre.android.point_mainapp_demo.app.databinding.PointMainappDemoAppActivityRefundsBinding

class RefundsActivity : AppCompatActivity() {

    private var binding: PointMainappDemoAppActivityRefundsBinding? = null
    private val viewModel by viewModels<RefundsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PointMainappDemoAppActivityRefundsBinding.inflate(layoutInflater)
        binding?.run { setContentView(root) }
        configRefundsButton()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.successResult.observe(this) { result ->
            showRefundResult(result)
        }
        viewModel.errorResult.observe(this) { error ->
            showRefundError(error)
        }
    }

    private fun configRefundsButton() {
        binding?.sendRefundActionButton?.setOnClickListener {
            val paymentId = binding?.paymentIdEditText?.text?.toString()
            val accessToken = binding?.accessTokenEditText?.text?.toString()
            viewModel.performRefund(paymentId?.toLong() ?: 0L, accessToken ?: "")
        }
    }

    private fun showRefundResult(result: String) {
        binding?.refundsResult?.text =
            String.format(resources.getString(R.string.point_mainapp_demo_app_lab_refund_result_text, result))
    }

    private fun showRefundError(error: String) {
        binding?.refundsResult?.text = error
    }
}
