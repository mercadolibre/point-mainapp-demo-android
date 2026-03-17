package com.mercadolibre.android.point_mainapp_demo.app.view.printer

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mercadolibre.android.point_integration_sdk.nativesdk.MPManager
import com.mercadolibre.android.point_integration_sdk.nativesdk.message.utils.doIfError
import com.mercadolibre.android.point_integration_sdk.nativesdk.message.utils.doIfSuccess
import com.mercadolibre.android.point_mainapp_demo.app.R
import com.mercadolibre.android.point_mainapp_demo.app.databinding.ActivityPrinterCombinedBinding
import com.mercadolibre.android.point_mainapp_demo.app.util.gone
import com.mercadolibre.android.point_mainapp_demo.app.util.hideKeyboard
import com.mercadolibre.android.point_mainapp_demo.app.util.toast
import com.mercadolibre.android.point_mainapp_demo.app.util.visible
import com.mercadolibre.android.point_mainapp_demo.app.view.payment.dialog.SelectionPaymentMethodDialogFragment

/**
 * Activity que permite probar dos modos de impresión combinada:
 * 1. Sequential (En línea): Imprime custom tag y bitmap uno tras otro sin esperar callbacks
 * 2. Callback-based (Con callback): Imprime el bitmap después del callback de éxito del custom tag
 */
class PrinterCombinedActivity : AppCompatActivity() {

    private var lastPaymentMethodSelected: String? = null
    private var clearPaymentMethodList: Boolean = true

    val binding: ActivityPrinterCombinedBinding by lazy {
        ActivityPrinterCombinedBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupHardcodedContent()
        setupListeners()
        setupPaymentMethodSelectedTextView()
        hideKeyboard()
    }

    /**
     * Pre-pobla el campo de texto con el contenido hardcodeado del custom tag
     */
    private fun setupHardcodedContent() {
        binding.inputText.setText(HARDCODED_CUSTOM_TAG_CONTENT)
    }

    private fun setupListeners() {
        binding.apply {
            btnPrintSequential.setOnClickListener {
                printBothSequential()
            }

            btnPrintWithCallback.setOnClickListener {
                printBothWithCallback()
            }

            btnGetPaymentMethod.setOnClickListener {
                onClickGetPaymentMethodAction()
            }

            btnRestart.setOnClickListener {
                restartScreen()
            }
        }
    }

    /**
     * Modo 1: Dispara N custom tags en línea SIN esperar callbacks entre ellos.
     * No incluye bitmap para aislar el comportamiento del SDK con múltiples
     * llamadas concurrentes de impresión custom tag.
     */
    private fun printBothSequential() {
        if (!validateInputs()) return
        val count = getPrintCount()
        var completed = 0
        var finalized = false

        binding.apply {
            progressCircular.visible()
            groupInputs.gone()
            btnRestart.gone()
            iconDescription.setImageResource(R.drawable.point_mainapp_demo_app_black_ic_print)
            tvResults.text = getString(R.string.printing_sequential_mode_n, count)
        }

        val content = binding.inputText.text.toString()
        val printPdf417InBoleta = binding.checkboxPrintPdf417Boleta.isChecked

        repeat(count) { index ->
            val label = if (count > 1) " [${index + 1}/$count]" else ""

            MPManager.bitmapPrinter.print(content, lastPaymentMethodSelected, printPdf417InBoleta) { response ->
                response
                    .doIfSuccess { result ->
                        appendResult("✓ Custom Tag$label: $result")
                        completed++
                        if (completed == count && !finalized) {
                            finalized = true
                            onPrintsCompleted()
                        }
                    }
                    .doIfError { error ->
                        appendResult("✗ Custom Tag$label error: ${error.message.orEmpty()}")
                        if (!finalized) {
                            finalized = true
                            onPrintError(error.message.orEmpty())
                        }
                    }
            }
        }
    }

    /**
     * Modo 2: Encadena N pares de impresión (Custom Tag → Bitmap) de forma recursiva.
     * Cada par espera el callback exitoso del anterior antes de disparar el siguiente.
     */
    private fun printBothWithCallback() {
        if (!validateInputs()) return
        val count = getPrintCount()

        binding.apply {
            progressCircular.visible()
            groupInputs.gone()
            btnRestart.gone()
            iconDescription.setImageResource(R.drawable.point_mainapp_demo_app_black_ic_print)
            tvResults.text = getString(R.string.printing_callback_mode_n, count)
        }

        printPairWithCallback(currentPair = 1, totalPairs = count)
    }

    /**
     * Imprime un par (Custom Tag → Bitmap) y al completarse exitosamente
     * dispara el siguiente par hasta alcanzar [totalPairs].
     */
    private fun printPairWithCallback(currentPair: Int, totalPairs: Int) {
        val content = binding.inputText.text.toString()
        val printPdf417InBoleta = binding.checkboxPrintPdf417Boleta.isChecked
        val label = if (totalPairs > 1) " [$currentPair/$totalPairs]" else ""

        MPManager.bitmapPrinter.print(content, lastPaymentMethodSelected, printPdf417InBoleta) { response ->
            response
                .doIfSuccess { customTagResult ->
                    appendResult("✓ Custom Tag$label: $customTagResult")

                    val inputStream = resources.openRawResource(R.raw.point_mainapp_demo_app_ic_datafono)
                    BitmapFactory.decodeStream(inputStream)?.run {
                        MPManager.bitmapPrinter.print(this) { bitmapResponse ->
                            bitmapResponse
                                .doIfSuccess { bitmapResult ->
                                    appendResult("✓ Bitmap$label: $bitmapResult")
                                    if (currentPair < totalPairs) {
                                        printPairWithCallback(currentPair + 1, totalPairs)
                                    } else {
                                        onPrintsCompleted()
                                    }
                                }
                                .doIfError { error ->
                                    onPrintError("Bitmap$label error: ${error.message.orEmpty()}")
                                }
                        }
                    } ?: onPrintError("Error al decodificar el bitmap")
                }
                .doIfError { error ->
                    onPrintError("Custom Tag$label error: ${error.message.orEmpty()}")
                }
        }
    }

    /**
     * Valida que el campo de texto no esté vacío
     */
    private fun validateInputs(): Boolean {
        val content = binding.inputText.text.toString()
        if (content.isEmpty()) {
            toast(getString(R.string.error_empty_content))
            return false
        }
        return true
    }

    /**
     * Lee el campo de cantidad de repeticiones.
     * Si está vacío o inválido, retorna el valor por defecto (2).
     * Rango válido: 1–50.
     */
    private fun getPrintCount(): Int {
        val raw = binding.inputPrintCount.text.toString().toIntOrNull() ?: DEFAULT_PRINT_COUNT
        return raw.coerceIn(1, MAX_PRINT_COUNT)
    }

    /**
     * Agrega un resultado al TextView de resultados
     */
    private fun appendResult(message: String) {
        binding.tvResults.apply {
            val currentText = text.toString()
            text = if (currentText.isEmpty()) {
                message
            } else {
                "$currentText\n$message"
            }
        }
    }

    /**
     * Callback cuando ambas impresiones se completaron exitosamente
     */
    private fun onPrintsCompleted() {
        binding.apply {
            progressCircular.gone()
            iconDescription.setImageResource(R.drawable.point_mainapp_demo_app_ic_done)
            btnRestart.visible()
            appendResult("\n${getString(R.string.all_prints_completed)}")
        }
    }

    /**
     * Callback cuando ocurre un error en alguna impresión
     */
    private fun onPrintError(message: String) {
        binding.apply {
            progressCircular.gone()
            iconDescription.setImageResource(R.drawable.point_mainapp_demo_app_ic_error)
            btnRestart.visible()
            toast(message)
        }
    }

    /**
     * Reinicia la pantalla para una nueva prueba
     * Mantiene el contenido hardcodeado en el campo de texto
     */
    private fun restartScreen() {
        binding.apply {
            inputText.setText(HARDCODED_CUSTOM_TAG_CONTENT)
            inputPrintCount.text?.clear()
            checkboxPrintPdf417Boleta.isChecked = false
            iconDescription.setImageResource(R.drawable.point_mainapp_demo_app_black_ic_print)
            tvResults.text = ""
            progressCircular.gone()
            groupInputs.visible()
            btnRestart.gone()
        }
    }

    /**
     * Gestión del método de pago
     */
    private fun onClickGetPaymentMethodAction() {
        binding.apply {
            clearPaymentMethodList = clearPaymentMethodList.not()
            if (clearPaymentMethodList) {
                clearPaymentMethodAction()
            } else {
                btnGetPaymentMethod.text = getString(R.string.point_mainapp_demo_app_clear_label)
                launchPaymentMethodDialog()
            }
        }
    }

    private fun clearPaymentMethodAction() {
        binding.apply {
            btnGetPaymentMethod.text = getString(R.string.point_mainapp_demo_app_lab_get_payment_method_action)
            lastPaymentMethodSelected = null
            setupPaymentMethodSelectedTextView()
        }
    }

    private fun launchPaymentMethodDialog() {
        val dialog = SelectionPaymentMethodDialogFragment.newInstance()
        dialog.onListenerPaymentMethod = { paymentMethod ->
            lastPaymentMethodSelected = paymentMethod
            setupPaymentMethodSelectedTextView()
        }
        dialog.show(supportFragmentManager, "SelectionPaymentMethodDialogFragment")
    }

    private fun setupPaymentMethodSelectedTextView() {
        binding.tvPaymentMethodSelected.apply {
            lastPaymentMethodSelected?.let { paymentMethod ->
                visible()
                text = String.format(
                    getString(R.string.point_mainapp_demo_app_home_print_payment_method_selected_custom_tag),
                    paymentMethod
                )
            } ?: gone()
        }
    }

    companion object {
        private const val DEFAULT_PRINT_COUNT = 2
        private const val MAX_PRINT_COUNT = 50

        /**
         * Contenido hardcodeado del custom tag para las pruebas de impresión.
         * - {br} = Salto de línea
         * - {center} = Centrar texto
         * - {w}{/w} = Texto en negrita/ancho
         * - {s}{/s} = Texto pequeño
         */
        private const val HARDCODED_CUSTOM_TAG_CONTENT =
            "{br}--------------------------------{br}{center}{w} COMPROBANTE DE ENTREGA{/w}{br}{br}{s} Nro pedido :26649{/s}{br}{s} Tienda: Mall Plaza Vespucio-QA{/s}{br}--------------------------------{br}{s}***ITEM(S) DESPACHO***{/s}{br}{s}SKU / ARTICULO                    CANTIDAD    {/s}{br}{s}----------------------------------------------{/s}{br}{s}4065432630504 / BALON FUTBOL ADIDAS WUCL LGE EHV240424   1{br}{s}ENTREGAR: 06/06/2024{/s}{br}{s}DIRECCION: METROPOLITANA Cerro Navia test 12345  {/s}{br}{s}RECIBE: Diego Diaz{/s}{br}{s}entrega a cliente en horario am{/s}{br}--------------------------------{br}"
    }
}
