package com.mercadolibre.android.point_mainapp_demo.app

import android.content.Context
import com.mercadolibre.android.point_mainapp_demo.app.actions.ActionModel
import com.mercadolibre.android.point_mainapp_demo.app.actions.ActionTypes
import com.mercadolibre.android.point_mainapp_demo.app.actions.ActionsProvicer
import com.mercadolibre.android.point_mainapp_demo.app.view.info.SmartInfoActivity
import com.mercadolibre.android.point_mainapp_demo.app.view.payment.launcher.PaymentLauncherActivity

object ActionsProviderImpl : ActionsProvicer {
    override fun getActions(context: Context): List<ActionModel> {
        return listOf(
            ActionModel(
                title = "PaymentFlow",
                icon = context.getDrawable(R.drawable.point_mainapp_demo_app_ic_money),
                action = mapOf(ActionTypes.INTENT_EXPLICIT to PaymentLauncherActivity::class.java)
            ),
            ActionModel(
                title = "Info",
                icon = context.getDrawable(R.drawable.point_mainapp_demo_app_ic_bluetooth),
                action = mapOf(ActionTypes.INTENT_EXPLICIT to SmartInfoActivity::class.java)
            )
        )
    }
}