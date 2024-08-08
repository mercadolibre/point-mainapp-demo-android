package com.mercadolibre.android.point_mainapp_demo.app.view.camera

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mercadolibre.android.point_integration_sdk.nativesdk.MPManager
import com.mercadolibre.android.point_integration_sdk.nativesdk.camera.domain.ScanType
import com.mercadolibre.android.point_integration_sdk.nativesdk.camera.provider.entities.CameraScannerStatus
import com.mercadolibre.android.point_integration_sdk.nativesdk.message.utils.doIfError
import com.mercadolibre.android.point_integration_sdk.nativesdk.message.utils.doIfSuccess
import com.mercadolibre.android.point_mainapp_demo.app.R
import com.mercadolibre.android.point_mainapp_demo.app.databinding.PointMainappDemoAppActivityCameraScannerBinding
import com.mercadolibre.android.point_mainapp_demo.app.util.gone
import com.mercadolibre.android.point_mainapp_demo.app.util.visible

class CameraScannerActivity : AppCompatActivity() {

    private val binding: PointMainappDemoAppActivityCameraScannerBinding by lazy {
        PointMainappDemoAppActivityCameraScannerBinding.inflate(layoutInflater)
    }

    private val cameraScanner by lazy {
        MPManager.cameraScanner
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.pointMainappDemoAppCameraQrScannerInitBtn.setOnClickListener {
            cameraScanner.initQRCodeScanner(this)
        }

        binding.pointMainappDemoAppCameraBarcodeScannerInitBtn.setOnClickListener {
            cameraScanner.initBarcodeScanner(this)
        }

        binding.pointMainappDemoAppCameraQrScannerNewInitBtn.setOnClickListener {
            launchScanner(ScanType.CAMERA_SCANNER_QR)
        }

        binding.pointMainappDemoAppNewCameraBarcodeScannerInitBtn.setOnClickListener {
            launchScanner(ScanType.CAMERA_SCANNER_BARCODE)
        }
        binding.pointMainappDemoAppCameraScannerBackBtn.setOnClickListener {
            showButton()
            binding.pointMainappDemoAppCameraScannerResult.gone()
            binding.pointMainappDemoAppStateImg.gone()
        }
    }

    private fun launchScanner(scanType: ScanType) {
        cameraScanner.launchScanner(scanType) { response ->
            response.doIfSuccess {
                cameraResultSuccess(it.message.orEmpty())
            }.doIfError {
                cameraResultError(it.message.orEmpty())
            }
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
                                cameraResultSuccess(qrResult.message.orEmpty())
                            }

                            CameraScannerStatus.Error -> {
                                cameraResultError(qrResult.message.orEmpty())
                            }

                            CameraScannerStatus.Unknown -> {
                                cameraResultSuccess(qrResult.message.orEmpty())
                            }
                        }
                    }
                }
                .doIfError { error ->
                    cameraResultError(error.message.orEmpty())
                }
        }
    }

    private fun cameraResultSuccess(result: String) {
        binding.run {
            pointMainappDemoAppStateImg.visible()
            pointMainappDemoAppCameraScannerBackBtn.visible()
            pointMainappDemoAppCameraScannerResult.visible()
            goneButton()
            val resultCamera = getString(R.string.point_mainapp_demo_app_cammera_scanner_start_scan_success).format(result)
            pointMainappDemoAppCameraScannerResult.text = resultCamera
            pointMainappDemoAppStateImg.setImageResource(R.drawable.point_mainapp_demo_app_ic_check_white)
        }
    }

    private fun cameraResultError(error: String) {
        binding.run {
            pointMainappDemoAppStateImg.visible()
            pointMainappDemoAppCameraScannerBackBtn.visible()
            pointMainappDemoAppCameraScannerResult.visible()
            goneButton()
            val errorCamera = getString(R.string.point_mainapp_demo_app_cammera_scanner_start_scan_error).format(error)
            pointMainappDemoAppCameraScannerResult.text = errorCamera
            pointMainappDemoAppStateImg.setImageResource(R.drawable.point_mainapp_demo_app_ic_error)
        }
    }

    private fun goneButton() {
        binding.run {
            pointMainappDemoAppCameraQrScannerInitBtn.gone()
            pointMainappDemoAppCameraBarcodeScannerInitBtn.gone()
            pointMainappDemoAppNewCameraBarcodeScannerInitBtn.gone()
            pointMainappDemoAppCameraQrScannerNewInitBtn.gone()
        }
    }

    private fun showButton() {
        binding.run {
            pointMainappDemoAppCameraQrScannerInitBtn.visible()
            pointMainappDemoAppCameraBarcodeScannerInitBtn.visible()
            pointMainappDemoAppNewCameraBarcodeScannerInitBtn.visible()
            pointMainappDemoAppCameraQrScannerNewInitBtn.visible()
            pointMainappDemoAppCameraScannerBackBtn.gone()
            pointMainappDemoAppCameraScannerResult.gone()
            pointMainappDemoAppStateImg.gone()
        }
    }
}
