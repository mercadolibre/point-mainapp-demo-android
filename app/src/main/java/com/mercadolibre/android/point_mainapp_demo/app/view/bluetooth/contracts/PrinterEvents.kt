package com.mercadolibre.android.point_mainapp_demo.app.view.bluetooth.contracts

import com.mercadolibre.android.point_integration_sdk.nativesdk.bluetoothclient.provider.entities.BluetoothDeviceModel

sealed class PrinterEvents {
    class IsLoading(val isVisible: Boolean) : PrinterEvents()
    class OutPutResult(val resultMessage: String) : PrinterEvents()
    class LaunchPrinterSelector(val printerList: List<BluetoothDeviceModel>) : PrinterEvents()
    object DataEmpty : PrinterEvents()
}
