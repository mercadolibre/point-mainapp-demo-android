package com.mercadolibre.android.point_mainapp_demo.app.view.camera

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mercadolibre.android.point_integration_sdk.nativesdk.MPManager
import com.mercadolibre.android.point_integration_sdk.nativesdk.camera.provider.entities.CameraScannerStatus
import com.mercadolibre.android.point_mainapp_demo.app.R
import com.mercadolibre.android.point_mainapp_demo.app.databinding.PointMainappDemoAppActivityCameraScannerBinding

class CameraScannerActivity : AppCompatActivity() {

    private val binding: PointMainappDemoAppActivityCameraScannerBinding by lazy {
        PointMainappDemoAppActivityCameraScannerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.pointMainappDemoAppCameraScannerInitBtn.setOnClickListener {
            MPManager.cameraScanner.initQRCodeScanner(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val qrResult = MPManager.cameraScanner.handleQrResponse(resultCode, data)
        binding.pointMainappDemoAppStateImg.visibility = View.VISIBLE
        when (qrResult.status) {
            CameraScannerStatus.Ok -> {
                binding.pointMainappDemoAppCameraScannerResult.text = "Lectura Exitosa: ${qrResult.message}"
                binding.pointMainappDemoAppStateImg.setImageResource(R.drawable.point_mainapp_demo_app_ic_check_white)
            }

            CameraScannerStatus.Error -> {
                binding.pointMainappDemoAppCameraScannerResult.text = "Error: ${qrResult.message}"
                binding.pointMainappDemoAppStateImg.setImageResource(R.drawable.point_mainapp_demo_app_ic_error)
            }

            CameraScannerStatus.Unknown -> {
                "Indefinido: ${qrResult.message}"
                binding.pointMainappDemoAppStateImg.visibility = View.GONE

            }
        }
    }
}
