package com.mercadolibre.android.point_mainapp_demo.app.view.bluetooth.printer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mercadolibre.android.point_mainapp_demo.app.R
import com.mercadolibre.android.point_mainapp_demo.app.databinding.ActivityPrinterTestBinding

class PrinterTestActivity : AppCompatActivity() {

    private val binding: ActivityPrinterTestBinding by lazy { ActivityPrinterTestBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}