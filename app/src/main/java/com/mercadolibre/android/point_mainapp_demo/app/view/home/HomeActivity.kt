package com.mercadolibre.android.point_mainapp_demo.app.view.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mercadolibre.android.point_mainapp_demo.app.ActionsProviderImpl
import com.mercadolibre.android.point_mainapp_demo.app.BuildConfig
import com.mercadolibre.android.point_mainapp_demo.app.R
import com.mercadolibre.android.point_mainapp_demo.app.actions.contract.HomeActions
import com.mercadolibre.android.point_mainapp_demo.app.actions.view.HomeActionAdapter
import com.mercadolibre.android.point_mainapp_demo.app.databinding.PointMainappDemoAppActivityHomeBinding
import com.mercadolibre.android.point_mainapp_demo.app.util.launchActivity

class HomeActivity : AppCompatActivity() {

    private val binding: PointMainappDemoAppActivityHomeBinding by lazy {
        PointMainappDemoAppActivityHomeBinding.inflate(layoutInflater)
    }

    private val actionAdapter: HomeActionAdapter by lazy {
        HomeActionAdapter(::handlerActionItem)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.buttonLaunchNewPayment.setOnClickListener {
            val deepLink = "mercadopago_fcu://integrators_new_payment?" +
                    "amount=6.00" +
                    "&description=Pago%20de%20prueba" +
                    "&integrator_type=istanbul" +
                    "&payment_intent_id=INTENT-001" +
                    "&external_reference=ORDER-12345" +
                    "&marketplace_id=my_marketplace" +
                    "&platform_id=my_platform" +
                    "&integrator_id=my_integrator" +
                    "&need_timer=false" +
                    "&initial_time=45" +
                    "&print_on_terminal=true" +
                    "&notification_url=https://myserver.com/webhook" +
                    "&success_url=myapp://success" +
                    "&fail_url=myapp://fail"

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(deepLink)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION
            }
            startActivity(intent)
        }
        binding.run { setContentView(root) }
    }

    private fun getVersionName() {
        val versionName = BuildConfig.VERSION_NAME
        binding.pointMainappDemoAppVersion.text =
            getString(R.string.point_mainapp_demo_app_version_name, versionName)
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
}
