package com.mercadolibre.android.point_mainapp_demo.app

import android.app.Application

/**
 * Main Application class that extends from Application to execute the start method only once.
 */
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
