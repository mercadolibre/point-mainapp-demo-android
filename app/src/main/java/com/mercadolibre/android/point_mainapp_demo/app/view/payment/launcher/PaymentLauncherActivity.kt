package com.mercadolibre.android.point_mainapp_demo.app.view.payment.launcher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mercadolibre.android.point_integration_sdk.nativesdk.MPManager
import com.mercadolibre.android.point_integration_sdk.nativesdk.message.utils.doIfError
import com.mercadolibre.android.point_integration_sdk.nativesdk.message.utils.doIfSuccess
import com.mercadolibre.android.point_integration_sdk.nativesdk.payment.data.PaymentFlowRequestData
import com.mercadolibre.android.point_integration_sdk.nativesdk.payment.data.PaymentMethod
import com.mercadolibre.android.point_mainapp_demo.app.R
import com.mercadolibre.android.point_mainapp_demo.app.databinding.PointMainappDemoAppActivityPaymentLauncherBinding
import com.mercadolibre.android.point_mainapp_demo.app.util.gone
import com.mercadolibre.android.point_mainapp_demo.app.util.hideKeyboard
import com.mercadolibre.android.point_mainapp_demo.app.util.launchActivity
import com.mercadolibre.android.point_mainapp_demo.app.util.toast
import com.mercadolibre.android.point_mainapp_demo.app.util.visible
import com.mercadolibre.android.point_mainapp_demo.app.view.payment.adapter.PaymentMethodAdapter
import com.mercadolibre.android.point_mainapp_demo.app.view.payment.launcher.PaymentFlowInstallmentsActivity.Companion.AMOUNT
import com.mercadolibre.android.point_mainapp_demo.app.view.payment.launcher.PaymentFlowInstallmentsActivity.Companion.DESCRIPTION
import com.mercadolibre.android.point_mainapp_demo.app.view.payment.launcher.PaymentFlowInstallmentsActivity.Companion.PAYMENT_METHOD
import com.mercadolibre.android.point_mainapp_demo.app.view.payment.launcher.PaymentFlowInstallmentsActivity.Companion.PRINT_ON_TERMINAL
import com.mercadolibre.android.point_mainapp_demo.app.view.payment.models.PaymentMethodModel

/** Main activity class */
class PaymentLauncherActivity : AppCompatActivity() {

    lateinit var binding: PointMainappDemoAppActivityPaymentLauncherBinding
    private val paymentFlow = MPManager.paymentFlow
    private val paymentTool = MPManager.paymentMethodsTools
    private var lastPaymentMethodSelected: PaymentMethod? = null
    private var clearPaymentMethodList: Boolean = true
    private var isPrintOnTerminal: Boolean = true
    private val paymentMethodAdapter by lazy {
        PaymentMethodAdapter {
            lastPaymentMethodSelected = PaymentMethod.valueOf(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PointMainappDemoAppActivityPaymentLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerviewPaymentMethod.apply {
            layoutManager = LinearLayoutManager(
                this@PaymentLauncherActivity, LinearLayoutManager.VERTICAL, false
            )
            adapter = paymentMethodAdapter
        }

        configPaymentButton()
        isAutomaticPrintOnTerminal()
    }

    private fun isAutomaticPrintOnTerminal() =
        binding.checkboxIsAutomaticPrinting.setOnCheckedChangeListener { _, isChecked ->
            isPrintOnTerminal = isChecked
        }

    private fun configPaymentButton() {

        binding.apply {
            getPaymentMethodActionButton.setOnClickListener {
                hideKeyboard()
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
                launchPaymentFlow(amount, description)
            }
        }
    }

    private fun launchPaymentFlow(amount: String?, description: String?) = when {
        amount.isNullOrEmpty() -> ERROR_INVALID_AMOUNT.setLayoutError()

        isCreditCard() -> launchActivity(
            PaymentFlowInstallmentsActivity::class.java, bundleOf(
                PAYMENT_METHOD to lastPaymentMethodSelected?.name,
                AMOUNT to amount,
                DESCRIPTION to description,
                PRINT_ON_TERMINAL to isPrintOnTerminal
            )
        )

        else -> launchPaymentFlowIntent(
            amount = amount, description = description
        )
    }

    private fun isCreditCard() = lastPaymentMethodSelected == PaymentMethod.CREDIT_CARD

    private fun configPaymentMethodList() {
        paymentTool.getPaymentMethods { response ->
            response.doIfSuccess { result ->
                val paymentMethodList = result.map { PaymentMethodModel(name = it.name) }
                paymentMethodAdapter.submitList(paymentMethodList)
            }.doIfError { error ->
                toast(error.message.orEmpty())
            }
        }
    }

    private fun launchPaymentFlowIntent(
        amount: String, description: String?
    ) {
        binding.paymentProgressBar.visible()
        paymentFlow.launchPaymentFlow(
            PaymentFlowRequestData(
                amount.toDouble(),
                description,
                paymentMethod = lastPaymentMethodSelected,
                printOnTerminal = isPrintOnTerminal
            )
        ) { response ->
            binding.paymentProgressBar.gone()
            response.doIfSuccess {
                showSnackBar(MESSAGE_PAYMENT_SUCCESS.format(it.paymentReference))
            }.doIfError {
                it.message?.let { message ->
                    showSnackBar(MESSAGE_PAYMENT_CANCELED.format(message), true)
                }
            }
        }
    }

    private fun String?.setLayoutError() {

        binding.amountInputLayout.apply {
            isCounterEnabled = true
            error = this@setLayoutError
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

    private fun showSnackBar(message: String, isCanceled: Boolean = false) {
        Snackbar.make(
            binding.root, message, Snackbar.ANIMATION_MODE_SLIDE
        ).setBackgroundTint(getBackgroundColorSnackBar(isCanceled)).show()
    }

    private fun getBackgroundColorSnackBar(canceled: Boolean): Int = if (canceled) {
        getColor(R.color.design_default_color_error)
    } else {
        getColor(R.color.doneColor)
    }

    companion object {
        private const val ERROR_INVALID_AMOUNT = "Amount is null or empty"
        private const val MESSAGE_PAYMENT_CANCELED = "Your payment was %s"
        private const val MESSAGE_PAYMENT_SUCCESS = "Your payment reference is: %s"
    }
}
