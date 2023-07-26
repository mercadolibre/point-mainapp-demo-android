package com.mercadolibre.android.point_mainapp_demo.app.view.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mercadolibre.android.point_integration_sdk.nativesdk.MPManager
import com.mercadolibre.android.point_mainapp_demo.app.ActionsProviderImpl
import com.mercadolibre.android.point_mainapp_demo.app.actions.contract.HomeActions
import com.mercadolibre.android.point_mainapp_demo.app.actions.view.HomeActionAdapter
import com.mercadolibre.android.point_mainapp_demo.app.databinding.PointMainappDemoAppActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private val binding: PointMainappDemoAppActivityHomeBinding by lazy {
        PointMainappDemoAppActivityHomeBinding.inflate(layoutInflater)
    }

    private val actionAdapter: HomeActionAdapter by lazy {
        HomeActionAdapter(::handlerActionItem)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.run { setContentView(root) }
        actionAdapter.submitList(ActionsProviderImpl.getActions(this@HomeActivity))
        setRecyclerView()
    }

    private fun setRecyclerView() {
        binding.rvActions.apply {
            layoutManager = LinearLayoutManager(
                this@HomeActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = actionAdapter
        }
    }

    private fun handlerActionItem(action: HomeActions) {
        when (action) {
            is HomeActions.LaunchActivity -> launchActivity(action.activity)
            is HomeActions.LaunchBtUi -> action.actionManager.bluetoothUiSettings.launch(this@HomeActivity)
        }
    }

    private fun <T> launchActivity(destination: Class<T>) {
        Intent(this, destination).run {
            startActivity(this)
        }
    }
}
