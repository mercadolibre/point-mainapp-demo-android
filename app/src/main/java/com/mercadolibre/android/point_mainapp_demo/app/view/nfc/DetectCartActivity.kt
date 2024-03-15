package com.mercadolibre.android.point_mainapp_demo.app.view.nfc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mercadolibre.android.point_integration_sdk.nativesdk.MPManager
import com.mercadolibre.android.point_integration_sdk.nativesdk.data.nfc.enums.NfcAvailableLed
import com.mercadolibre.android.point_integration_sdk.nativesdk.message.utils.MPResponse
import com.mercadolibre.android.point_integration_sdk.nativesdk.message.utils.doIfError
import com.mercadolibre.android.point_integration_sdk.nativesdk.message.utils.doIfSuccess
import com.mercadolibre.android.point_integration_sdk.nativesdk.nfc.exception.NfcTimeoutException
import com.mercadolibre.android.point_mainapp_demo.app.R
import com.mercadolibre.android.point_mainapp_demo.app.databinding.ActivityDetectCartBinding
import com.mercadolibre.android.point_mainapp_demo.app.util.gone
import com.mercadolibre.android.point_mainapp_demo.app.util.launchActivity
import com.mercadolibre.android.point_mainapp_demo.app.util.toast
import com.mercadolibre.android.point_mainapp_demo.app.util.visible

class DetectCartActivity : AppCompatActivity() {

    private val binding: ActivityDetectCartBinding by lazy {
        ActivityDetectCartBinding.inflate(layoutInflater)
    }

    private val nfcConfig = MPManager.nfcConfig

    private var serialInfo = byteArrayOf()

    private var operationCompleted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initDetectionNfc()
        setListeners()
        binding.buttonNfcDetection.setOnClickListener {
            operationClose()
        }
    }

    private fun initDetectionNfc() {
        setViews(DETECTING, "Detecting card NFC...")
        nfcConfig.nfcOpenTools.open { mpResponse ->
            mpResponse.doIfSuccess {
                operationDetectNfc()
            }.doIfError {
                toast("Operation open: ${it.message}")
            }
        }
    }

    private fun operationDetectNfc() {
        operationLedOn(NfcAvailableLed.LED_2)
        nfcConfig.nfcDetectTools.detectCard(TIMEOUT) { mpResponse ->
            mpResponse.doIfSuccess { result ->
                setViews(
                    SUCCESS,
                    MESSAGE_DETECTION_DESCRIPTION.format(result.serialInfo.toString())
                )
            }.doIfError { exception ->
                resultException(exception)
            }
        }
    }

    private fun operationLedOn(led: NfcAvailableLed) {
        if (!operationCompleted) {
            nfcConfig.nfcLedOnTools.ledOn(nfcAvailableLed = led) { mpResponse ->
                onResultOperation(mpResponse, "Operation led on:")
                operationCompleted = true
            }
        }
    }

    private fun onResultOperation(mpResponse: MPResponse<Boolean>, message: String) {
        mpResponse.doIfSuccess {
            toast("$message ${it}")
        }.doIfError {
            toast("$message ${it.message}")
        }
    }

    private fun resultException(exception: Exception) {
        if (exception is NfcTimeoutException) {
            setViews("Timeout", exception.message.orEmpty())
        } else {
            setViews("Error", exception.message.orEmpty())
        }
    }

    private fun setViews(title: String, description: String) {
        binding.apply {
            "$title\n$description".also { textNfcDetectionDescription.text = it }
            when (title) {

                DETECTING -> imageNfcDetectionCard.setImageResource(R.drawable.point_mainapp_demo_app_ic_nfc)

                SUCCESS -> {
                    imageNfcDetectionCard.setImageResource(R.drawable.point_mainapp_demo_app_ic_check_white)
                    cardViewNfcReader.visible()
                    cardViewNfcWriter.visible()
                    buttonNfcDetection.gone()
                    operationLedOff()
                }

                else -> imageNfcDetectionCard.setImageResource(R.drawable.point_mainapp_demo_app_ic_error)
            }
        }

    }

    private fun operationLedOff() {
        nfcConfig.nfcLedOffTools.ledOff { mpResponse ->
            mpResponse.doIfSuccess {
                toast("Led off")
            }.doIfError {
                toast("Led off: ${it.message}")
            }
        }
    }

    private fun operationClose() {
        nfcConfig.nfcCloseTools.close { mpResponse ->
            mpResponse.doIfSuccess {
                finish()
            }.doIfError {
                toast("Close: ${it.message}")
            }
        }
    }

    private fun setListeners() {
        binding.cardViewNfcReader.setOnClickListener {
            launchActivity(ReadNfcActivity::class.java, Bundle().apply {
                putByteArray("serialInfo", serialInfo)
            })
        }
        binding.cardViewNfcWriter.setOnClickListener {
            launchActivity(WriteNfcActivity::class.java)
        }
    }

    companion object {
        private const val DETECTING = "Detecting..."
        private const val SUCCESS = "Success"
        private const val TIMEOUT = 10000L
        private const val MESSAGE_DETECTION_DESCRIPTION =
            "We have detected the card %s, select an option to continue, hold the card in the reader while the operation is executed."
    }
}
