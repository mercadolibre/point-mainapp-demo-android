package com.mercadolibre.android.point_mainapp_demo.app.view.payment.launcher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mercadolibre.android.point_integration_sdk.nativesdk.MPManager
import com.mercadolibre.android.point_integration_sdk.nativesdk.message.utils.doIfError
import com.mercadolibre.android.point_integration_sdk.nativesdk.message.utils.doIfSuccess
import com.mercadolibre.android.point_integration_sdk.nativesdk.payment.data.PaymentFlowData
import com.mercadolibre.android.point_integration_sdk.nativesdk.utils.ifLet
import com.mercadolibre.android.point_mainapp_demo.app.databinding.PointMainappDemoAppActivityPaymentFlowInstallmetsBinding
import com.mercadolibre.android.point_mainapp_demo.app.util.gone
import com.mercadolibre.android.point_mainapp_demo.app.util.visible
import com.mercadolibre.android.point_mainapp_demo.app.view.payment.adapter.PaymentInstallmentAdapter

class PaymentFlowInstallmentsActivity : AppCompatActivity() {

    private val binding: PointMainappDemoAppActivityPaymentFlowInstallmetsBinding by lazy {
        PointMainappDemoAppActivityPaymentFlowInstallmetsBinding.inflate(layoutInflater)
    }

    private val paymentFlow = MPManager.paymentFlow

    private var amount: String? = null

    private var paymentMethod: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        amount = intent.getStringExtra(AMOUNT)
        paymentMethod = intent.getStringExtra(PAYMENT_METHOD)
        setView()
        val adapter = PaymentInstallmentAdapter { installmentAmount ->
            ifLet(amount, paymentMethod) { (amountValue, paymentMethodValue) ->
                paymentFlowInstallment(
                    amountValue,
                    paymentMethodValue,
                    installmentAmount.installment.toString()
                )
            }
        }
        getRecyclerView(adapter)
    }

    private fun setView() {
        binding.apply {
            textAmount.text = amount.orEmpty()
            textAmountTotal.text = TOTAL_AMOUNT
        }
    }

    private fun getRecyclerView(adapter: PaymentInstallmentAdapter) {
        ifLet(amount, paymentMethod) { (amountValue, paymentMethodValue) ->
            MPManager.paymentInstallmentTools.getInstallmentsAmount({ mpResponse ->
                mpResponse.doIfSuccess { installments ->
                    adapter.submitList(installments)
                    setRecyclerView(adapter)
                }.doIfError {
                    paymentFlowInstallment(
                        amount = amountValue,
                        paymentMethod = paymentMethodValue
                    )
                }
            }, amountValue)
        }
    }

    private fun setRecyclerView(adapter: PaymentInstallmentAdapter) {
        binding.rvListInstallments.apply {
            layoutManager = LinearLayoutManager(
                this@PaymentFlowInstallmentsActivity,
                RecyclerView.VERTICAL,
                false
            )
            this.adapter = adapter
        }
    }

    private fun paymentFlowInstallment(
        amount: String,
        paymentMethod: String,
        installment: String? = null
    ) {
        val paymentFlowData = PaymentFlowData(
            amount = amount,
            paymentMethod = paymentMethod,
            installments = installment?.toIntOrNull(),
            intentSuccess = paymentFlow.buildCallbackUri(
                callback = "mercadopago://launcher_native_app",
                methodCallback = "success",
                metadata = hashMapOf("message" to "testSuccess"),
                appID = "demo.app"
            ),
            intentError = paymentFlow.buildCallbackUri(
                callback = "mercadopago://launcher_native_app",
                methodCallback = "error",
                metadata = hashMapOf("message" to "testError"),
                appID = "demo.app"
            )
        )
        launchPaymentInstallment(paymentFlowData)
    }

    private fun launchPaymentInstallment(paymentFlowData: PaymentFlowData) {
        paymentFlow.launchPaymentFlowActivity(paymentFlowData, this) { response ->
            response.doIfError {
                setOnError(it.message)
            }
        }
    }

    private fun setOnError(message: String?) {
        binding.apply {
            textAmount.gone()
            textAmountTotal.gone()
            rvListInstallments.gone()
            textException.visible()
            imageErrorInstallment.visible()
            btnGoBack.visible()
            message.let { textException.text = it }
            btnGoBack.setOnClickListener { finish() }
        }
    }

    companion object {
        internal const val AMOUNT = "amount"
        internal const val PAYMENT_METHOD = "payment_method"
        internal const val TOTAL_AMOUNT = "Total Amount"
    }
}
