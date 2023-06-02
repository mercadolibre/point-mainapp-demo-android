package com.mercadolibre.android.point_mainapp_demo.app.view.info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mercadolibre.android.point_integration_sdk.nativesdk.MPManager
import com.mercadolibre.android.point_mainapp_demo.app.R
import com.mercadolibre.android.point_mainapp_demo.app.databinding.PointMainappDemoAppActivitySmartInfoBinding

class SmartInfoActivity : AppCompatActivity() {

    private var binding: PointMainappDemoAppActivitySmartInfoBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = PointMainappDemoAppActivitySmartInfoBinding.inflate(layoutInflater)
        binding?.run {
            setContentView(root)
            configSmartInfo()
        }
    }

    private fun configSmartInfo() {
        runCatching {
            MPManager.smartInformationTools.getInformation { smartInformation ->
                with(smartInformation.smartDevice) {
                    val serialNumber = getString(R.string.point_mainapp_demo_app_serial_number_text).format(serialNumber)
                    val brandName = getString(R.string.point_mainapp_demo_app_brand_name_text).format(brandName)
                    val modelName = getString(R.string.point_mainapp_demo_app_model_name_text).format(modelName)

                    binding?.run {
                        mainappDemoAppSerialNumberText.text = serialNumber
                        mainappDemoAppBrandNameText.text = brandName
                        mainappDemoAppModelNameText.text = modelName
                    }
                }

                with(smartInformation.integration) {
                    val sdkVersion = getString(R.string.point_mainapp_demo_app_sdk_version_text).format(nativeSdkVersion)
                    binding?.mainappDemoAppSdkVersionText?.text = sdkVersion
                }
            }
        }
    }
}
