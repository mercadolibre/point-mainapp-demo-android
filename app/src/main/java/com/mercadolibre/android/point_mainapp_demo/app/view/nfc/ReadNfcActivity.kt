package com.mercadolibre.android.point_mainapp_demo.app.view.nfc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mercadolibre.android.point_integration_sdk.nativesdk.MPManager
import com.mercadolibre.android.point_integration_sdk.nativesdk.message.utils.doIfError
import com.mercadolibre.android.point_integration_sdk.nativesdk.message.utils.doIfSuccess
import com.mercadolibre.android.point_mainapp_demo.app.databinding.ActivityReadNfcBinding
import com.mercadolibre.android.point_mainapp_demo.app.util.launchActivity
import com.mercadolibre.android.point_mainapp_demo.app.util.toast

class ReadNfcActivity : AppCompatActivity() {

    private val binding: ActivityReadNfcBinding by lazy {
        ActivityReadNfcBinding.inflate(layoutInflater)
    }

    private val nfcConfig = MPManager.nfcConfig

    private var serialInfo = byteArrayOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        intent.data?.let {
            serialInfo = it.toString().toByteArray()
        }
        setViews()
        setListeners()
    }

    private fun initReadNfc() {
        //TODO: implementar lectura de nfc
    }

    private fun setViews() {
        "Reading card NFC whit serial info: $serialInfo".also {
            binding.textNfcReaderDescription.text = it
        }
    }

    private fun setListeners() {
        binding.buttonNfcReadCard.setOnClickListener {
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
}
