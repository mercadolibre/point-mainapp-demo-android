package com.mercadolibre.android.point_mainapp_demo.app.view.bluetooth.adapter

import android.bluetooth.BluetoothDevice.BOND_BONDED
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mercadolibre.android.point_integration_sdk.nativesdk.bluetoothclient.provider.entities.BluetoothDeviceModel
import com.mercadolibre.android.point_mainapp_demo.app.R
import com.mercadolibre.android.point_mainapp_demo.app.databinding.PointMainappDemoAppItemDevicesBluetoothBinding
import com.mercadolibre.android.point_mainapp_demo.app.util.visible

internal class BluetoothDeviceViewHolder(private val viewBinding: PointMainappDemoAppItemDevicesBluetoothBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {

    internal fun render(item: BluetoothDeviceModel, callback: (String) -> Unit) {
        if (item.boundState == BOND_BONDED) {
            renderPairDevices(item, callback)
        } else {
            renderNotPairDevices(item, callback)
        }
        viewBinding.textViewPrimaryText.text = item.name.ifEmpty { item.address }
    }

    private fun renderPairDevices(item: BluetoothDeviceModel, callback: (String) -> Unit) {
        viewBinding.apply {
            imageViewBluetoothState.setImageResource(
                if (item.isConnected) R.drawable.point_mainapp_demo_app_ic_bluetooth_connected else R.drawable.point_mainapp_demo_app_ic_bluetooth_available
            )

            if (item.isConnected) {
                textViewSecondaryText.visible()
                textViewSecondaryText.text = viewBinding.root.context.getString(
                    R.string.point_mainapp_demo_app__bluetooth_devices_connect
                )
            }

            textViewPrimaryText.setOnClickListener {
                callback(item.address)
            }
        }
    }

    private fun renderNotPairDevices(item: BluetoothDeviceModel, callback: (String) -> Unit) {
        viewBinding.apply {
            imageViewBluetoothState.setImageResource(
                R.drawable.point_mainapp_demo_app_ic_bluetooth_available
            )
            textViewPrimaryText.setOnClickListener {
                callback(item.address)
            }
        }
    }

    companion object {
        internal fun from(parent: ViewGroup): BluetoothDeviceViewHolder {
            val binding = PointMainappDemoAppItemDevicesBluetoothBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return BluetoothDeviceViewHolder(binding)
        }
    }
}
