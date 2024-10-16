package com.mercadolibre.android.point_mainapp_demo.app.view.printer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mercadolibre.android.point_integration_sdk.nativesdk.MPManager
import com.mercadolibre.android.point_integration_sdk.nativesdk.message.utils.doIfError
import com.mercadolibre.android.point_integration_sdk.nativesdk.message.utils.doIfSuccess
import com.mercadolibre.android.point_mainapp_demo.app.R
import com.mercadolibre.android.point_mainapp_demo.app.databinding.ActivityPrinterCustomTagBinding
import com.mercadolibre.android.point_mainapp_demo.app.util.gone
import com.mercadolibre.android.point_mainapp_demo.app.util.hideKeyboard
import com.mercadolibre.android.point_mainapp_demo.app.util.toast
import com.mercadolibre.android.point_mainapp_demo.app.util.visible
import com.mercadolibre.android.point_mainapp_demo.app.view.payment.adapter.PaymentMethodAdapter
import com.mercadolibre.android.point_mainapp_demo.app.view.payment.models.PaymentMethodModel

class PrinterCustomTagActivity : AppCompatActivity() {
    private var lastPaymentMethodSelected: String? = null
    private var clearPaymentMethodList: Boolean = true

    val binding: ActivityPrinterCustomTagBinding by lazy {
        ActivityPrinterCustomTagBinding.inflate(layoutInflater)
    }

    private val paymentMethodAdapter by lazy {
        PaymentMethodAdapter {
            lastPaymentMethodSelected = it
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        onPrintCustomTag()
        onGetPaymentMethod()
        setupRecyclerView()
        hideKeyboard()
    }

    private fun setupRecyclerView() {
        binding.recyclerviewPaymentMethod.apply {
            layoutManager = LinearLayoutManager(
                this@PrinterCustomTagActivity, LinearLayoutManager.HORIZONTAL, false
            )
            adapter = paymentMethodAdapter
        }
    }

    private fun onPrintCustomTag() {
        binding.apply {
            printImageCustomTag.setOnClickListener {
                onClickPrintAction()
            }
        }
    }

    private fun onGetPaymentMethod() {
        binding.apply {
            getPaymentMethodCustomTag.setOnClickListener {
                onClickGetPaymentMethodAction()
            }
        }
    }

    private fun onClickPrintAction() {
        binding.apply {
            if (printImageCustomTag.text.toString() == getString(R.string.point_mainapp_demo_app_text_button_printer_custom_tag)) {
                progressCircular.visible()
                groupPrintImageCustomTag.gone()
                printImageCustomTag()
            } else {
                finish()
            }
        }
    }

    private fun ActivityPrinterCustomTagBinding.onClickGetPaymentMethodAction() {
        hideKeyboard()
        clearPaymentMethodList = clearPaymentMethodList.not()
        if (clearPaymentMethodList) {
            getPaymentMethodCustomTag.text =
                getString(R.string.point_mainapp_demo_app_lab_get_payment_method_action)
            lastPaymentMethodSelected = null
            paymentMethodAdapter.clear()
        } else {
            getPaymentMethodCustomTag.text =
                getString(R.string.point_mainapp_demo_app_clear_label)
            configPaymentMethodList()
        }
    }


    private fun configPaymentMethodList() {
        MPManager.paymentMethodsTools.getPaymentMethods { response ->
            response.doIfSuccess { result ->
                val paymentMethodList = result.map { PaymentMethodModel(name = it.name) }
                paymentMethodAdapter.submitList(paymentMethodList)
            }.doIfError { error ->
                toast(error.message.orEmpty())
            }
        }
    }


    private fun printImageCustomTag() {
        val content = binding.inputText.text.toString()
        val printPdf417InBoleta = binding.checkboxPrintPdf417Boleta.isChecked
        MPManager.bitmapPrinter.print(
            content,
        ) { response ->
            response
                .doIfSuccess { result ->
                    onResultSuccess(result)
                }
                .doIfError { error ->
                    onResultFailure(error.message.orEmpty())
                    toast(error.message.orEmpty())
                }
        }
    }

    private fun onResultSuccess(result: String) {

        binding.apply {
            progressCircular.gone()
            iconDescription.setImageResource(R.drawable.point_mainapp_demo_app_ic_done)
            descriptionPrinterBitmap.text = getString(
                R.string.point_mainapp_demo_app_home_result_successful_description_printer_bitmap,
                result
            )
            printImageCustomTag.text = TEXT_BUTTON_ACTION
        }
    }

    private fun onResultFailure(message: String) {

        binding.apply {
            progressCircular.gone()
            iconDescription.setImageResource(R.drawable.point_mainapp_demo_app_ic_error)
            descriptionPrinterBitmap.text =
                getString(
                    R.string.point_mainapp_demo_app_home_result_failure_description_printer_bitmap,
                    message
                )
            printImageCustomTag.text = TEXT_BUTTON_ACTION
        }
    }

    companion object {
        private const val TEXT_BUTTON_ACTION = "Go to start"
    }
}
