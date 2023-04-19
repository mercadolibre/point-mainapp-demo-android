package com.mercadolibre.android.point_mainapp_demo.app.view.bluetooth.printer

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.mercadolibre.android.point_mainapp_demo.app.R
import com.mercadolibre.android.point_mainapp_demo.app.databinding.PointMainappDemoAppActivityPrinterTestBinding
import com.mercadolibre.android.point_mainapp_demo.app.view.bluetooth.contracts.PrinterEvents
import com.mercadolibre.android.point_mainapp_demo.app.view.bluetooth.viewmodels.BluetoothPrinterViewModel

class PrinterTestActivity : AppCompatActivity(), PrinterSelectorCallback {

    private val binding: PointMainappDemoAppActivityPrinterTestBinding by lazy {
        PointMainappDemoAppActivityPrinterTestBinding.inflate(
            layoutInflater
        )
    }

    private val viewModel by viewModels<BluetoothPrinterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setObserver()
        onClicksListener()
    }

    private fun onClicksListener() {
        binding.apply {
            buttonMakePrint.setOnClickListener {
                viewModel.makePrint(textFieldDataToPrint.text.toString())
            }
        }
    }

    private fun setObserver() {
        viewModel.printerEventLiveDataLiveData.observe(this) { event ->
            when (event) {
                is PrinterEvents.IsLoading -> binding.progressIndicator.isVisible = event.isVisible
                is PrinterEvents.LaunchPrinterSelector -> {
                    val dialog = PrinterSelectorDialog.newInstance(event.printerList)
                    dialog.show(supportFragmentManager, "printer")
                }
                is PrinterEvents.OutPutResult -> Snackbar.make(binding.root, event.resultMessage, Snackbar.LENGTH_LONG).show()
                PrinterEvents.DataEmpty -> Snackbar
                    .make(binding.root, getString(R.string.point_mainapp_demo_app_error_msg_data_empty), Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onListenerOptionPrinter(address: String) {
        viewModel.makePrint(binding.textFieldDataToPrint.text.toString(), address)
    }
}