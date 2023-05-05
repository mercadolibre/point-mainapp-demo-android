package com.mercadolibre.android.point_mainapp_demo.app.view.printer

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.coroutineScope
import com.mercadolibre.android.point_integration_sdk.nativesdk.MPManager
import com.mercadolibre.android.point_mainapp_demo.app.R
import com.mercadolibre.android.point_mainapp_demo.app.databinding.ActivityPrinterBitmapBinding
import com.mercadolibre.android.point_mainapp_demo.app.util.gone
import com.mercadolibre.android.point_mainapp_demo.app.util.visible
import kotlinx.coroutines.launch

class PrinterBitmapActivity : AppCompatActivity() {

    private var binding: ActivityPrinterBitmapBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrinterBitmapBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        onPrintImageBitmap()
    }

    private fun onPrintImageBitmap() {
        binding?.apply {

            printImageBitmap.setOnClickListener {

                onActionButton()
            }
        }
    }

    private fun onActionButton() {
        binding?.apply {
            if (printImageBitmap.text.toString() == getString(R.string.point_mainapp_demo_app_text_button_printer_bitmap)) {
                progressCircular.visible()
                printImageBitmap()
            } else {
                finish()
            }
        }
    }

    private fun printImageBitmap() {

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_datafono)

        lifecycle.coroutineScope.launch {
            MPManager.bitmapPrinter.makePrint(bitmap) {
                onResultPrinterBitmap(it)
            }
        }
    }

    private fun onResultPrinterBitmap(message: String) {

        when(message) {

            PRINT_SUCCESS -> onResultSuccess()
            else -> onResultFailure(message)
        }
    }

    private fun onResultSuccess() {

        binding?.apply {
            progressCircular.gone()
            iconDescription.setImageResource(R.drawable.point_mainapp_demo_app_ic_done)
            descriptionPrinterBitmap.text = getString(R.string.point_mainapp_demo_app_home_result_successful_description_printer_bitmap)
            printImageBitmap.text = TEXT_BUTTON_ACTION
        }
    }

    private fun onResultFailure(message: String) {

        binding?.apply {
            progressCircular.gone()
            iconDescription.setImageResource(R.drawable.point_mainapp_demo_app_ic_error)
            descriptionPrinterBitmap.text = getString(R.string.point_mainapp_demo_app_home_result_failure_description_printer_bitmap, message)
            printImageBitmap.text = TEXT_BUTTON_ACTION
        }
    }

    companion object {
        private const val TEXT_BUTTON_ACTION = "Go to start"
        private const val PRINT_SUCCESS = "Success"
    }
}
