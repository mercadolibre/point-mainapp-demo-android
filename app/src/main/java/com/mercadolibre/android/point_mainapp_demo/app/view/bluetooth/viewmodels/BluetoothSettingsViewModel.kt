package com.mercadolibre.android.point_mainapp_demo.app.view.bluetooth.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadolibre.android.point_integration_sdk.nativesdk.MPManager
import com.mercadolibre.android.point_integration_sdk.nativesdk.bluetoothclient.provider.contracts.states.DiscoveryEventsResult
import com.mercadolibre.android.point_integration_sdk.nativesdk.bluetoothclient.provider.entities.BluetoothDeviceModel
import com.mercadolibre.android.point_integration_sdk.nativesdk.message.utils.doIfError
import com.mercadolibre.android.point_integration_sdk.nativesdk.message.utils.doIfSuccess
import com.mercadolibre.android.point_mainapp_demo.app.view.bluetooth.contracts.BluetoothSettingsEvents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class BluetoothSettingsViewModel : ViewModel() {

    private val _bluetoothSettingLiveData = MutableStateFlow<BluetoothSettingsEvents>(BluetoothSettingsEvents.Init)
    val bluetoothSettingLiveData = _bluetoothSettingLiveData.asStateFlow()

    fun registerConnectObserver() {
        MPManager.bluetooth.connectObserver.registerObserver { result ->
            result
                .doIfSuccess { pair ->
                    val bluetoothDeviceModel = pair.first
                    _bluetoothSettingLiveData.value = BluetoothSettingsEvents.ConnectDevicesResult(bluetoothDeviceModel)
                }.doIfError { exception ->
                    _bluetoothSettingLiveData.value = BluetoothSettingsEvents.Error(exception)
                }
        }
    }

    fun getCurrentStateBluetooth() {
        MPManager.bluetooth.ignitor.getCurrentState { response ->
            response
                .doIfSuccess { result ->
                    _bluetoothSettingLiveData.value = BluetoothSettingsEvents.IgnitorCurrentState(result)
                }.doIfError { exception ->
                    _bluetoothSettingLiveData.value = BluetoothSettingsEvents.Error(exception)
                }
        }
    }

    fun ignitorBluetooth(ignitor: Boolean) {
        MPManager.bluetooth.ignitor.run {
            if (ignitor) {
                turnOn { response ->
                    response
                        .doIfSuccess { result ->
                            _bluetoothSettingLiveData.value = BluetoothSettingsEvents.IgnitorLaunchResult(result)
                        }
                        .doIfError { exception ->
                            _bluetoothSettingLiveData.value = BluetoothSettingsEvents.Error(exception)
                        }
                }
            } else {
                turnOff { response ->
                    response
                        .doIfSuccess { result ->
                            _bluetoothSettingLiveData.value = BluetoothSettingsEvents.IgnitorLaunchResult(result)
                        }
                        .doIfError { exception ->
                            _bluetoothSettingLiveData.value = BluetoothSettingsEvents.Error(exception)
                        }
                }
            }
        }
    }

    fun getPairDevices() {
        viewModelScope.launch(Dispatchers.IO) {
            MPManager.bluetooth.discover.getPairDevices { response ->
                response
                    .doIfSuccess { result ->
                        _bluetoothSettingLiveData.value = BluetoothSettingsEvents.DiscoveryPairDevicesResult(result)
                    }
                    .doIfError { exception ->
                        _bluetoothSettingLiveData.value = BluetoothSettingsEvents.Error(exception)
                    }
            }
        }
    }

    fun startBluetoothDiscovery() {
        viewModelScope.launch(Dispatchers.IO) {
            MPManager.bluetooth.discover.startDiscovery(object : DiscoveryEventsResult {
                override fun discoveryStarted() {
                    _bluetoothSettingLiveData.value = BluetoothSettingsEvents.DiscoveryStarted
                }

                override fun internalError(error: Exception) {
                    _bluetoothSettingLiveData.value = BluetoothSettingsEvents.Error(error)
                }

                override fun discoveryEnded() {
                    _bluetoothSettingLiveData.value = BluetoothSettingsEvents.DiscoveryEnded
                }

                override fun devicesFound(devices: BluetoothDeviceModel) {
                    _bluetoothSettingLiveData.value = BluetoothSettingsEvents.DiscoveryDevicesFound(devices)
                }

                override fun devicesUpdate(devices: BluetoothDeviceModel) {
                    _bluetoothSettingLiveData.value = BluetoothSettingsEvents.DiscoveryDevicesUpdate(devices)
                }
            })
        }
    }

    @SuppressLint("LogConditional")
    fun pairingDevices(address: String, needPair: Boolean) {
        Log.i(TAG, "pairingDevices: devices address --> $address, needPair --> $needPair")
        viewModelScope.launch(Dispatchers.IO) {
            if (needPair) {
                MPManager.bluetooth.paring.pairDevices(address) { response ->
                    response
                        .doIfSuccess { resultPair ->
                            Log.i(TAG, "pairDevices: callback response ${resultPair.first}")
                            _bluetoothSettingLiveData.value = BluetoothSettingsEvents.PairingDevicesStatus(resultPair)
                        }
                        .doIfError { exception ->
                            _bluetoothSettingLiveData.value = BluetoothSettingsEvents.Error(exception)
                        }
                }
            } else {
                MPManager.bluetooth.paring.unPairDevices(address) { response ->
                    response
                        .doIfSuccess { resultPair ->
                            Log.i(TAG, "unPairDevices: callback response ${resultPair.first}")
                            _bluetoothSettingLiveData.value = BluetoothSettingsEvents.PairingDevicesStatus(resultPair)
                        }.doIfError { exception ->
                            _bluetoothSettingLiveData.value = BluetoothSettingsEvents.Error(exception)
                        }
                }
            }
        }
    }

    companion object {
        const val TAG = "pairingDevicesWay"
    }
}
