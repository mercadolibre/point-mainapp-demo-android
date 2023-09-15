package com.mercadolibre.android.point_mainapp_demo.app.view.payment.launcher

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.mercadolibre.android.point_integration_sdk.nativesdk.MPManager
import com.mercadolibre.android.point_integration_sdk.nativesdk.message.utils.doIfError
import com.mercadolibre.android.point_integration_sdk.nativesdk.message.utils.doIfSuccess
import com.mercadolibre.android.point_integration_sdk.nativesdk.payment.data.PaymentMethod
import com.mercadolibre.android.point_mainapp_demo.app.R
import com.mercadolibre.android.point_mainapp_demo.app.databinding.PointMainappDemoAppActivityPaymentLauncherBinding
import com.mercadolibre.android.point_mainapp_demo.app.util.launchActivity
import com.mercadolibre.android.point_mainapp_demo.app.util.toast
import com.mercadolibre.android.point_mainapp_demo.app.view.payment.adapter.PaymentMethodAdapter
import com.mercadolibre.android.point_mainapp_demo.app.view.payment.launcher.PaymentFlowInstallmentsActivity.Companion.AMOUNT
import com.mercadolibre.android.point_mainapp_demo.app.view.payment.launcher.PaymentFlowInstallmentsActivity.Companion.PAYMENT_METHOD
import com.mercadolibre.android.point_mainapp_demo.app.view.payment.models.PaymentMethodModel

/** Main activity class */
class PaymentLauncherActivity : AppCompatActivity() {

    lateinit var binding: PointMainappDemoAppActivityPaymentLauncherBinding
    private val paymentFlow = MPManager.paymentFlow
    private val paymentTool = MPManager.paymentMethodsTools
    private var lastPaymentMethodSelected: String? = null
    private var clearPaymentMethodList: Boolean = true
    private val paymentMethodAdapter by lazy {
        PaymentMethodAdapter {
            lastPaymentMethodSelected = it
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PointMainappDemoAppActivityPaymentLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerviewPaymentMethod.apply {
            layoutManager = LinearLayoutManager(
                this@PaymentLauncherActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = paymentMethodAdapter
        }

        configPaymentButton()
    }

    private fun configPaymentButton() {

        binding.apply {
            getPaymentMethodActionButton.setOnClickListener {
                clearPaymentMethodList = clearPaymentMethodList.not()
                if (clearPaymentMethodList) {
                    getPaymentMethodActionButton.text =
                        getString(R.string.point_mainapp_demo_app_lab_get_payment_method_action)
                    lastPaymentMethodSelected = null
                    paymentMethodAdapter.clear()
                } else {
                    getPaymentMethodActionButton.text =
                        getString(R.string.point_mainapp_demo_app_clear_label)
                    configPaymentMethodList()
                }
            }
            sendPaymentActionButton.setOnClickListener {
                val amount = amountEditText.text?.toString()
                val description = binding.descriptionEditText.text?.toString()
                if (lastPaymentMethodSelected == PaymentMethod.CREDIT_CARD.name.lowercase()) {
                    launchActivity(
                        PaymentFlowInstallmentsActivity::class.java,
                        bundleOf(
                            PAYMENT_METHOD to lastPaymentMethodSelected,
                            AMOUNT to amount
                        )
                    )
                } else {
                    amount?.let {
                        launchPaymentFlowIntent(
                            amount = it,
                            description = description,
                            context = this@PaymentLauncherActivity
                        )
                    }
                }
            }
        }
    }

    private fun configPaymentMethodList() {
        paymentTool.getPaymentMethods { response ->
            response
                .doIfSuccess { result ->
                    val paymentMethodList = result.map { PaymentMethodModel(name = it.name) }
                    paymentMethodAdapter.submitList(paymentMethodList)
                }
                .doIfError { error ->
                    toast(error.message.orEmpty())
                }
        }
    }

    private fun launchPaymentFlowIntent(
        amount: String,
        description: String?,
        context: Context,
    ) {
        val uriSuccess = paymentFlow.buildCallbackUri(
            "mercadopago://launcher_native_app",
            "success",
            hashMapOf("attr" to "123"),
            "demo_app"
        )
        val uriError = paymentFlow.buildCallbackUri(
            "mercadopago://launcher_native_app",
            "error",
            hashMapOf("attr" to "456"),
            "demo_app"
        )

        paymentFlow.launchPaymentFlowActivity(
            amount,
            description,
            uriSuccess,
            uriError,
            context,
            lastPaymentMethodSelected
        ) { response ->
            response.doIfError { error ->
                error.message?.let { errorMessage -> setLayoutError(errorMessage) }
            }
        }
    }

    private fun setLayoutError(message: String?) {

        binding.amountInputLayout.apply {
            isCounterEnabled = true
            error = message
        }

        listenerIconError()
    }

    private fun listenerIconError() {

        binding.amountInputLayout.apply {
            setErrorIconOnClickListener {
                isErrorEnabled = false
            }
        }
    }
}
