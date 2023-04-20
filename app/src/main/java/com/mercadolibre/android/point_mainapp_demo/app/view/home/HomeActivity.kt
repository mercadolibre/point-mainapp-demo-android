package com.mercadolibre.android.point_mainapp_demo.app.view.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mercadolibre.android.point_integration_sdk.nativesdk.MPManager
import com.mercadolibre.android.point_mainapp_demo.app.databinding.PointMainappDemoAppActivityHomeBinding
import com.mercadolibre.android.point_mainapp_demo.app.view.bluetooth.BluetoothTestActivity
import com.mercadolibre.android.point_mainapp_demo.app.view.bluetooth.printer.PrinterTestActivity
import com.mercadolibre.android.point_mainapp_demo.app.view.payment.launcher.PaymentLauncherActivity
import com.mercadolibre.android.point_mainapp_demo.app.view.refunds.RefundsActivity

class HomeActivity : AppCompatActivity() {

    private var binding: PointMainappDemoAppActivityHomeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PointMainappDemoAppActivityHomeBinding.inflate(layoutInflater)
        binding?.run { setContentView(root) }

        configGoToPaymentButton()
        configGoToBluetoothTools()
        configGoToBluetoothSettingsUI()
        configGoToRefundsUI()
        configGoToPrinterTest()
    }

    private fun configGoToRefundsUI() {
        binding?.pointMainappDemoAppGoToRefunds?.setOnClickListener {
            launchActivity(RefundsActivity::class.java)
        }
    }

    private fun configGoToPaymentButton() {
        binding?.pointMainappDemoAppGoToPaymentButton?.setOnClickListener {
            launchActivity(PaymentLauncherActivity::class.java)
        }
    }

    private fun configGoToBluetoothTools() {
        binding?.pointMainappDemoAppGoToBluetoothTools?.setOnClickListener {
            launchActivity(BluetoothTestActivity::class.java)
        }
    }

    private fun configGoToBluetoothSettingsUI() {
        binding?.pointMainappDemoAppGoToBluetoothUiSettings?.setOnClickListener {
            MPManager.bluetoothUiSettings.launch(this@HomeActivity)
        }
    }

    private fun configGoToPrinterTest() {
        binding?.pointMainappDemoAppGoToPrinterTest?.setOnClickListener {
            launchActivity(PrinterTestActivity::class.java)
        }
    }

    private fun <T>launchActivity(destination: Class<T>) {
        Intent(this, destination).run {
            startActivity(this)
        }
    }
}
