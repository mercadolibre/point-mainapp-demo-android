package com.mercadolibre.android.point_mainapp_demo.app

import android.app.Application
import com.mercadolibre.android.point_integration_sdk.nativesdk.MPManager
import com.mercadolibre.android.point_integration_sdk.nativesdk.configurable.MPConfigBuilder

/**
 * Main Application class that extends from Application to execute the start method only once.
 */
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val config = MPConfigBuilder(this)
            .withBluetoothConfig()
            .withBluetoothUIConfig()
            .build()

        MPManager.initialize(this, config)
    }
}
