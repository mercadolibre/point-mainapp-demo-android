package com.mercadolibre.android.point_mainapp_demo.app.view.payment.launcher

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mercadolibre.android.point_integration_sdk.nativesdk.MPManager
import com.mercadolibre.android.point_integration_sdk.nativesdk.resolver.validate.amount.exception.IllegalAmountException
import com.mercadolibre.android.point_mainapp_demo.app.R
import com.mercadolibre.android.point_mainapp_demo.app.databinding.PointMainappDemoAppActivityPaymentLauncherBinding
import com.mercadolibre.android.point_mainapp_demo.app.view.payment.adapter.PaymentMethodAdapter
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
                try {
                    amount?.let {
                        launchPaymentFlowIntent(
                            amount = it,
                            description = description,
                            context = this@PaymentLauncherActivity
                        )
                    }
                } catch (e: IllegalAmountException) {
                    setLayoutError(e.message)
                }
            }
        }
    }

    private fun configPaymentMethodList() {
        val paymentMethodList =
            paymentTool.getPaymentMethods().map { PaymentMethodModel(name = it.name) }
        paymentMethodAdapter.submitList(paymentMethodList)
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
        )
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
