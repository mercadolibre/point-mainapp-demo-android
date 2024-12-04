package com.mercadolibre.android.point_mainapp_demo.app

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import com.mercadolibre.android.point_integration_sdk.nativesdk.MPManager
import com.mercadolibre.android.point_mainapp_demo.app.actions.contract.ActionsProvider
import com.mercadolibre.android.point_mainapp_demo.app.actions.contract.HomeActions
import com.mercadolibre.android.point_mainapp_demo.app.actions.model.ActionModel
import com.mercadolibre.android.point_mainapp_demo.app.view.bluetooth.BluetoothTestActivity
import com.mercadolibre.android.point_mainapp_demo.app.view.bluetooth.printer.PrinterTestActivity
import com.mercadolibre.android.point_mainapp_demo.app.view.camera.CameraScannerActivity
import com.mercadolibre.android.point_mainapp_demo.app.view.info.SmartInfoActivity
import com.mercadolibre.android.point_mainapp_demo.app.view.payment.launcher.PaymentLauncherActivity
import com.mercadolibre.android.point_mainapp_demo.app.view.printer.PrinterBitmapActivity
import com.mercadolibre.android.point_mainapp_demo.app.view.printer.PrinterCustomTagActivity
import com.mercadolibre.android.point_mainapp_demo.app.view.refunds.RefundsActivity

object ActionsProviderImpl : ActionsProvider {
    override fun getActions(context: Context): List<ActionModel> {
        return listOf(
            ActionModel(
                title = context.getString(R.string.point_mainapp_demo_app_go_to_payment),
                icon = AppCompatResources.getDrawable(
                    context,
                    R.drawable.point_mainapp_demo_app_ic_payments
                ),
                action = HomeActions.LaunchActivity(PaymentLauncherActivity::class.java)
            ),
            ActionModel(
                title = context.getString(R.string.point_mainapp_demo_app_button_bluetooth_tools),
                icon = AppCompatResources.getDrawable(
                    context,
                    R.drawable.point_mainapp_demo_app_ic_bluetooth
                ),
                action = HomeActions.LaunchActivity(BluetoothTestActivity::class.java)
            ),
            ActionModel(
                title = context.getString(R.string.point_mainapp_demo_app_button_bluetooth_ui_settings),
                icon = AppCompatResources.getDrawable(
                    context,
                    R.drawable.point_mainapp_demo_app_ic_bluetooth
                ),
                action = HomeActions.LaunchBtUi(MPManager)
            ),
            ActionModel(
                title = context.getString(R.string.point_mainapp_demo_app_button_refunds),
                icon = AppCompatResources.getDrawable(
                    context,
                    R.drawable.point_mainapp_demo_app_ic_payments
                ),
                action = HomeActions.LaunchActivity(RefundsActivity::class.java)
            ),
            ActionModel(
                title = context.getString(R.string.point_mainapp_demo_app_home_print_label),
                icon = AppCompatResources.getDrawable(
                    context,
                    R.drawable.point_mainapp_demo_app_black_ic_print
                ),
                action = HomeActions.LaunchActivity(PrinterTestActivity::class.java)
            ),
            ActionModel(
                title = context.getString(R.string.point_mainapp_demo_app_home_printer_bitmap),
                icon = AppCompatResources.getDrawable(
                    context,
                    R.drawable.point_mainapp_demo_app_black_ic_print
                ),
                action = HomeActions.LaunchActivity(PrinterBitmapActivity::class.java)
            ),
            ActionModel(
                title = context.getString(R.string.point_mainapp_demo_app_home_printer_custom_tag),
                icon = AppCompatResources.getDrawable(
                    context,
                    R.drawable.point_mainapp_demo_app_black_ic_print
                ),
                action = HomeActions.LaunchActivity(PrinterCustomTagActivity::class.java)
            ),
            ActionModel(
                title = context.getString(R.string.point_mainapp_demo_app_cammera_scanner_main_title),
                icon = AppCompatResources.getDrawable(
                    context,
                    R.drawable.point_mainapp_demo_app_ic_qr_code
                ),
                action = HomeActions.LaunchActivity(CameraScannerActivity::class.java)
            ),
            ActionModel(
                title = context.getString(R.string.point_mainapp_demo_app_smart_info_main_title),
                icon = AppCompatResources.getDrawable(
                    context,
                    R.drawable.point_mainapp_demo_app_ic_info
                ),
                action = HomeActions.LaunchActivity(SmartInfoActivity::class.java)
            ),
        )
    }
}
