package com.mercadolibre.android.point_mainapp_demo.app

import com.mercadolibre.android.point_mainapp_demo.app.actions.ActionsProvicer

object ActionsProviderImpl : ActionsProvicer {
    override fun getActions(context: Context): List<ActionModel> {
        return listOf(
            ActionModel(
                title = "PaymentFlow",
                icon = context.getDrawable(R.drawable.point_mainapp_demo_app_ic_money),
                action = mapOf(ActionTypes.INTENT_EXPLICIT to PaymentLauncherActivity::class.java)
            ),
            ActionModel(
                title = "Bluetooth tools",
                icon = context.getDrawable(R.drawable.point_mainapp_demo_app_ic_bluetooth),
                action = mapOf(ActionTypes.INTENT_EXPLICIT to PaymentLauncherActivity::class.java)
            ),
            ActionModel(
                title = "Bluetooth UI",
                icon = context.getDrawable(R.drawable.point_mainapp_demo_app_ic_bluetooth),
                action = mapOf(ActionTypes.INTENT_IMPLICIT to MPManager.bluetoothUiSettings)
            )
        )
    }
}