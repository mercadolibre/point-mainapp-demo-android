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

        val config = MPConfigBuilder(this, DEMO_APP_CLIENT_ID)
            .withBluetoothConfig()
            .withBluetoothUIConfig()
            .build()

        MPManager.initialize(this, config)
    }

    companion object {
        private const val DEMO_APP_CLIENT_ID = "0011223344"
    }
}
