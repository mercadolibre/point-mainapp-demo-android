package com.mercadolibre.android.point_mainapp_demo.app.view.nfc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mercadolibre.android.point_integration_sdk.nativesdk.MPManager
import com.mercadolibre.android.point_integration_sdk.nativesdk.message.utils.doIfError
import com.mercadolibre.android.point_integration_sdk.nativesdk.message.utils.doIfSuccess
import com.mercadolibre.android.point_mainapp_demo.app.databinding.ActivityWriteNfcBinding
import com.mercadolibre.android.point_mainapp_demo.app.util.launchActivity
import com.mercadolibre.android.point_mainapp_demo.app.util.toast

class WriteNfcActivity : AppCompatActivity() {

    private val binding: ActivityWriteNfcBinding by lazy {
        ActivityWriteNfcBinding.inflate(layoutInflater)
    }

    private val nfcConfig = MPManager.nfcConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setViews()
        setListeners()
    }

    private fun setListeners() {
        binding.buttonNfcWriterCard.setOnClickListener {
            operationClose()
        }
    }

    private fun operationClose() {
        nfcConfig.nfcCloseTools.close { mpResponse ->
            mpResponse.doIfSuccess {
                launchActivity(NfcActionActivity::class.java)
            }.doIfError {
                toast("Operation close: ${it.message}")
            }
        }
    }

    private fun setViews() {
        "Writing card NFC...".also {
            binding.textNfcWriterDescription.text = it
        }
    }
}
