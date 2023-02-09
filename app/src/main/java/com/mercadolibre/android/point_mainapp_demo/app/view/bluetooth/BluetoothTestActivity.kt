package com.mercadolibre.android.point_mainapp_demo.app.view.bluetooth

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mercadolibre.android.point_integration_sdk.nativesdk.bluetoothclient.provider.contracts.states.BluetoothBondState
import com.mercadolibre.android.point_integration_sdk.nativesdk.bluetoothclient.provider.entities.BluetoothDeviceModel
import com.mercadolibre.android.point_mainapp_demo.app.databinding.PointMainappDemoAppActivityBluetoothTestBinding
import com.mercadolibre.android.point_mainapp_demo.app.util.gone
import com.mercadolibre.android.point_mainapp_demo.app.util.visible
import com.mercadolibre.android.point_mainapp_demo.app.view.bluetooth.adapter.BluetoothDevicesAdapter
import com.mercadolibre.android.point_mainapp_demo.app.view.bluetooth.contracts.BluetoothSettingsEvents.*
import com.mercadolibre.android.point_mainapp_demo.app.view.bluetooth.viewmodels.BluetoothSettingsViewModel

/**
 * test activity bluetooth.
 * */
class BluetoothTestActivity : AppCompatActivity() {

    private val binding: PointMainappDemoAppActivityBluetoothTestBinding by lazy {
        PointMainappDemoAppActivityBluetoothTestBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<BluetoothSettingsViewModel>()

    private val adapterPairDevices by lazy {
        BluetoothDevicesAdapter {
            binding.progressbarDiscoveryStarted.visible()
            viewModel.pairingDevices(
                address = it,
                needPair = false
            )
        }
    }

    private val adapterAvailableDevices by lazy {
        BluetoothDevicesAdapter {
            binding.progressbarDiscoveryStarted.visible()
            viewModel.pairingDevices(
                address = it,
                needPair = true
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.getCurrentStateBluetooth()
        setObserver()
        onclickListener()
        setRecyclerView()
    }

    private fun setRecyclerView() {
        binding.apply {

            recyclerViewPairDevices.apply {
                adapter = adapterPairDevices
                layoutManager = LinearLayoutManager(this@BluetoothTestActivity, LinearLayoutManager.VERTICAL, false)
            }

            recyclerViewFoundDevices.apply {
                adapter = adapterAvailableDevices
                layoutManager = LinearLayoutManager(this@BluetoothTestActivity, LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    private fun onclickListener() {
        binding.btIgnitor.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked.not()) adapterAvailableDevices.removeAll()
            viewModel.ignitorBluetooth(isChecked)
        }
    }

    @SuppressLint("LogConditional")
    private fun setObserver() {
        lifecycle.coroutineScope.launchWhenStarted {
            viewModel.bluetoothSettingLiveData.collect { events ->
                Log.i("eventos emitidos", "setObserver: ${events::class.java.simpleName} ")
                when (events) {
                    DiscoveryEnded -> binding.progressbarDiscoveryStarted.gone()
                    is DiscoveryDevicesFound -> adapterAvailableDevices.let {
                        binding.groupDiscoveryDevices.visible()
                        it.addItemDevice(events.devices)
                    }
                    is DiscoveryDevicesUpdate -> adapterAvailableDevices.updateItemDevice(events.devices)
                    DiscoveryStarted -> binding.progressbarDiscoveryStarted.visible()
                    is DiscoveryPairDevicesResult -> pairDevicesResult(events.devicesList)
                    is IgnitorCurrentState -> updateIgnitorBluetooth(events.state)
                    is IgnitorLaunchResult -> canLaunchGetPairDevices(events.result)
                    is PairingDevicesStatus -> handlerParingResult(events.pair)
                    Init -> Unit
                }
            }
        }
    }

    private fun handlerParingResult(pair: Pair<BluetoothBondState, BluetoothDeviceModel>) {
        binding.progressbarDiscoveryStarted.gone()
        val bondState = pair.first
        val bluetoothDevice = pair.second

        val devicesFoundInAvailableList = adapterAvailableDevices.findDevice(bluetoothDevice)

        viewModel.getPairDevices()

        when (bondState) {
            BluetoothBondState.NONE -> {
                devicesFoundInAvailableList?.let {
                    adapterAvailableDevices.updateItemDevice(it)
                } ?: adapterAvailableDevices.addItemDevice(bluetoothDevice)
                Toast.makeText(this, "devices forget", Toast.LENGTH_SHORT).show()
            }
            BluetoothBondState.BONDING -> devicesFoundInAvailableList?.let {
                adapterAvailableDevices.updateItemDevice(it)
            }

            BluetoothBondState.BONDED -> devicesFoundInAvailableList?.let {
                adapterAvailableDevices.removeItemDevice(it)
                Toast.makeText(this, "pair devices", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun pairDevicesResult(list: List<BluetoothDeviceModel>) {

        binding.groupPairDevices.isVisible = list.isNotEmpty()

        adapterPairDevices.submitList(list)
    }

    private fun updateIgnitorBluetooth(state: Boolean) {
        binding.apply {
            btIgnitor.isChecked = state
        }
    }

    private fun canLaunchGetPairDevices(launch: Boolean) {
        binding.apply {
            if (launch) {
                viewModel.getPairDevices()
                viewModel.startBluetoothDiscovery()
            } else {
                groupPairDevices.gone()
                groupDiscoveryDevices.gone()
                adapterAvailableDevices.submitList(emptyList())
                adapterPairDevices.submitList(emptyList())
            }
        }
    }
}
