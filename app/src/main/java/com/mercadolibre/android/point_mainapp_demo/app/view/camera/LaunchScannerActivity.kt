package com.mercadolibre.android.point_mainapp_demo.app.view.camera

import android.os.Bundle
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.mercadolibre.android.point_integration_sdk.nativesdk.MPManager
import com.mercadolibre.android.point_integration_sdk.nativesdk.camera.domain.ScanType
import com.mercadolibre.android.point_integration_sdk.nativesdk.camera.provider.data.ScannerFlowRequestData
import com.mercadolibre.android.point_integration_sdk.nativesdk.message.utils.doIfError
import com.mercadolibre.android.point_integration_sdk.nativesdk.message.utils.doIfSuccess
import com.mercadolibre.android.point_mainapp_demo.app.R
import com.mercadolibre.android.point_mainapp_demo.app.databinding.ActivityLaunchScannerBinding

class LaunchScannerActivity : AppCompatActivity() {

    private val binding: ActivityLaunchScannerBinding by lazy {
        ActivityLaunchScannerBinding.inflate(layoutInflater)
    }

    private var scannerRequestBuilder = ScannerFlowRequestData(
        initialOrientation = ORIENTATION_PORTRAIT,
        isLanternOn = false
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupViews()
    }

    private fun setupViews() {
        setupScanButtons()
        setupLanternSwitch()
        setupOrientationSpinner()
        initializeViewsWithCurrentState()
    }

    private fun initializeViewsWithCurrentState() {
        binding.switchTurnOn.isChecked = false
        binding.valueInitialOrientation.setText(ORIENTATION_NONE)
        binding.valueInitialOrientation.setSimpleItems(
            resources.getStringArray(R.array.point_mainapp_demo_app_camera_scanner_orientation)
        )
    }

    private fun setupScanButtons() {
        binding.pointMainappDemoAppCameraQrScannerNewInitBtn.setOnClickListener {
            launchScanner(ScanType.CAMERA_SCANNER_QR)
        }

        binding.pointMainappDemoAppNewCameraBarcodeScannerInitBtn.setOnClickListener {
            launchScanner(ScanType.CAMERA_SCANNER_BARCODE)
        }
    }

    private fun setupLanternSwitch() {
        binding.switchTurnOn.apply {
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    thumbTintList = getColorStateList(R.color.primaryColor)
                    trackTintList = getColorStateList(R.color.primaryColor)
                } else {
                    thumbTintList = getColorStateList(R.color.grayDark)
                    trackTintList = getColorStateList(R.color.grayDark)
                }
                scannerRequestBuilder = scannerRequestBuilder.copy(
                    isLanternOn = isChecked
                )
            }
        }
    }

    private fun setupOrientationSpinner() {
        binding.valueInitialOrientation.apply {
            onItemClickListener = AdapterView.OnItemClickListener { _, view, position, _ ->
                val selectedOrientation = when (position) {
                    1 -> ORIENTATION_LANDSCAPE
                    else -> ORIENTATION_PORTRAIT
                }
                scannerRequestBuilder = scannerRequestBuilder.copy(
                    initialOrientation = selectedOrientation
                )
            }
        }
    }


    private fun launchScanner(scanType: ScanType) {
        try {
            val requestData = scannerRequestBuilder
            MPManager.cameraScanner.launchScanner(scanType, requestData) { response ->
                response
                    .doIfSuccess { result -> showFeedback(result.message.toString()) }
                    .doIfError { error -> showFeedback(error.message.toString(), isError = true) }
            }
        } catch (e: Exception) {
            showFeedback("Error initial scanner: ${e.message}", isError = true)
        }
    }

    private fun showFeedback(message: String, isError: Boolean = false) {
        val backgroundColor = if (isError) {
            getColor(R.color.design_default_color_error)
        } else {
            getColor(R.color.doneColor)
        }

        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(backgroundColor)
            .show()
    }

    companion object {
        private const val ORIENTATION_NONE = "None"
        private const val ORIENTATION_PORTRAIT = 1
        private const val ORIENTATION_LANDSCAPE = 0
    }
}
