package com.mercadolibre.android.point_mainapp_demo.app.view.bluetooth.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadolibre.android.point_integration_sdk.nativesdk.MPManager
import com.mercadolibre.android.point_mainapp_demo.app.view.bluetooth.contracts.PrinterEvents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BluetoothPrinterViewModel : ViewModel() {

    private val _printerEventLiveDataLiveData = MutableLiveData<PrinterEvents>()
    val printerEventLiveDataLiveData: LiveData<PrinterEvents> get() = _printerEventLiveDataLiveData

    var isPasteLargeText = false

    fun makePrint(stringToPrint: String, addressDevices: String? = null) {

        if (stringToPrint.isEmpty()) {
            _printerEventLiveDataLiveData.value = PrinterEvents.DataEmpty
            return
        }

        _printerEventLiveDataLiveData.value = PrinterEvents.IsLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            addressDevices?.let { address ->
                MPManager.bluetooth.printer.makePrint(stringToPrint, address) { results ->
                    resultBehavior(results)
                }
            } ?: MPManager.bluetooth.printer.makePrint(stringToPrint) { results ->
                resultBehavior(results)
            }
        }
    }

    private fun resultBehavior(result: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _printerEventLiveDataLiveData.postValue(PrinterEvents.IsLoading(false))
            if (result == NEED_SELECTION_DEVICE) {
                MPManager.bluetooth.discover.getPairPrinterDevices { listPrinter ->
                    _printerEventLiveDataLiveData.postValue(PrinterEvents.LaunchPrinterSelector(listPrinter))
                }
            } else {
                _printerEventLiveDataLiveData.postValue(PrinterEvents.OutputResult(result))
            }
        }
    }

    companion object {
        const val NEED_SELECTION_DEVICE = "NEED_SELECTION_DEVICE"
    }
}
