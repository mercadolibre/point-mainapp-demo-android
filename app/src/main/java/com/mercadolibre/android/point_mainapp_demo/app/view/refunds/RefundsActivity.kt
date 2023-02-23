package com.mercadolibre.android.point_mainapp_demo.app.view.refunds

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
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
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.result.observe(this) { result ->
            showRefundResult(result)
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
        binding?.refundsResult?.text = result
        closeKeyboard()
    }

    private fun closeKeyboard() {
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
