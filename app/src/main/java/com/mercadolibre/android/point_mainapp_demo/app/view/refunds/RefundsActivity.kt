package com.mercadolibre.android.point_mainapp_demo.app.view.refunds

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mercadolibre.android.point_mainapp_demo.app.databinding.PointMainappDemoAppActivityRefundsBinding

class RefundsActivity : AppCompatActivity() {

    private var binding: PointMainappDemoAppActivityRefundsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PointMainappDemoAppActivityRefundsBinding.inflate(layoutInflater)
        binding?.run { setContentView(root) }

        configRefundsButton()
    }

    private fun configRefundsButton() {
        binding?.sendRefundActionButton?.setOnClickListener {
            val amount = binding?.amountEditText?.text?.toString()
            val description = binding?.accessTokenEditText?.text?.toString()
        }
    }
}
