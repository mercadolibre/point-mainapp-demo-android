package com.mercadolibre.android.point_mainapp_demo.app.actions.contract

import com.mercadolibre.android.point_integration_sdk.nativesdk.MPManager
import com.mercadolibre.android.point_mainapp_demo.app.view.home.HomeActivity

sealed class HomeActions {
   class LaunchActivity(val activity: Class<*>) : HomeActions()
   class LaunchBtUi(val actionManager:MPManager) : HomeActions()
}
