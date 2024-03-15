package com.mercadolibre.android.point_mainapp_demo.app.view.nfc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mercadolibre.android.point_mainapp_demo.app.databinding.ActivityNfcActionBinding
import com.mercadolibre.android.point_mainapp_demo.app.util.launchActivity

class NfcActionActivity : AppCompatActivity() {

    private val binding: ActivityNfcActionBinding by lazy {
        ActivityNfcActionBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setListeners()
    }

    private fun setListeners() {
        binding.cardViewNfcDetection.setOnClickListener {
            launchActivity(DetectCartActivity::class.java)
        }
    }
}
