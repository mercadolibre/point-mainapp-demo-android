package com.mercadolibre.android.point_mainapp_demo.app.view.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mercadolibre.android.point_integration_sdk.nativesdk.MPManager
import com.mercadolibre.android.point_mainapp_demo.app.databinding.PointMainappDemoAppActivityHomeBinding
import com.mercadolibre.android.point_mainapp_demo.app.view.bluetooth.BluetoothTestActivity
import com.mercadolibre.android.point_mainapp_demo.app.view.payment.launcher.PaymentLauncherActivity

class HomeActivity : AppCompatActivity() {

    private var binding: PointMainappDemoAppActivityHomeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PointMainappDemoAppActivityHomeBinding.inflate(layoutInflater)
        binding?.run { setContentView(root) }

        configGoToPaymentButton()
        configGoToBluetoothTools()
        configGoToBluetoothSettingsUI()
    }

    private fun configGoToPaymentButton() {
        binding?.pointMainappDemoAppGoToPaymentButton?.setOnClickListener {
            Intent(this, PaymentLauncherActivity::class.java).run {
                startActivity(this)
            }
        }
    }

    private fun configGoToBluetoothTools() {
        binding?.pointMainappDemoAppGoToBluetoothTools?.setOnClickListener {
            Intent(this, BluetoothTestActivity::class.java).run {
                startActivity(this)
            }
        }
    }

    private fun configGoToBluetoothSettingsUI() {
        binding?.pointMainappDemoAppGoToBluetoothUiSettings?.setOnClickListener {
            MPManager.bluetoothUiSettings.launch(this@HomeActivity)
        }
    }
}
