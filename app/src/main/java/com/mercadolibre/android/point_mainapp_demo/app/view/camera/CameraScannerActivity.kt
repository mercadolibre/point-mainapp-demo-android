package com.mercadolibre.android.point_mainapp_demo.app.view.camera

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mercadolibre.android.point_integration_sdk.nativesdk.MPManager
import com.mercadolibre.android.point_integration_sdk.nativesdk.camera.provider.entities.CameraScannerStatus
import com.mercadolibre.android.point_integration_sdk.nativesdk.message.utils.doIfError
import com.mercadolibre.android.point_integration_sdk.nativesdk.message.utils.doIfSuccess
import com.mercadolibre.android.point_mainapp_demo.app.R
import com.mercadolibre.android.point_mainapp_demo.app.databinding.PointMainappDemoAppActivityCameraScannerBinding
import com.mercadolibre.android.point_mainapp_demo.app.util.toast

class CameraScannerActivity : AppCompatActivity() {

    private val binding: PointMainappDemoAppActivityCameraScannerBinding by lazy {
        PointMainappDemoAppActivityCameraScannerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.pointMainappDemoAppCameraQrScannerInitBtn.setOnClickListener {
            MPManager.cameraScanner.initQRCodeScanner(this)
        }

        binding.pointMainappDemoAppCameraBarcodeScannerInitBtn.setOnClickListener {
            MPManager.cameraScanner.initBarcodeScanner(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        MPManager.cameraScanner.handleScannerResponse(this, resultCode, data) { response ->
            response
                .doIfSuccess { qrResult ->
                    binding.run {
                        pointMainappDemoAppStateImg.visibility = View.VISIBLE
                        when (qrResult.status) {
                            CameraScannerStatus.Ok -> {
                                val result = getString(R.string.point_mainapp_demo_app_cammera_scanner_start_scan_success)
                                    .format(qrResult.message)
                                pointMainappDemoAppCameraScannerResult.text = result
                                pointMainappDemoAppStateImg.setImageResource(R.drawable.point_mainapp_demo_app_ic_check_white)
                            }

                            CameraScannerStatus.Error -> {
                                val error = getString(R.string.point_mainapp_demo_app_cammera_scanner_start_scan_error)
                                    .format(qrResult.message)
                                pointMainappDemoAppCameraScannerResult.text = error
                                pointMainappDemoAppStateImg.setImageResource(R.drawable.point_mainapp_demo_app_ic_error)
                            }

                            CameraScannerStatus.Unknown -> {
                                val message = getString(R.string.point_mainapp_demo_app_cammera_scanner_start_scan_undefined)
                                    .format(qrResult.message)
                                pointMainappDemoAppCameraScannerResult.text = message
                                pointMainappDemoAppStateImg.visibility = View.GONE
                            }
                        }
                    }
                }
                .doIfError { error ->
                    toast(error.message.orEmpty())
                }
        }
    }
}
