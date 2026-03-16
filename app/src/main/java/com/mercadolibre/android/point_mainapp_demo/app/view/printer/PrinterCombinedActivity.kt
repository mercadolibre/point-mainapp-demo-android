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
     * Modo 1: Imprime las dos impresiones en línea (una línea debajo de otra)
     * Envía ambas impresiones SIN esperar el callback de la primera
     */
    private fun printBothSequential() {
        if (!validateInputs()) return

        binding.apply {
            progressCircular.visible()
            groupInputs.gone()
            btnRestart.gone()  // Asegurar que restart esté oculto
            iconDescription.setImageResource(R.drawable.point_mainapp_demo_app_black_ic_print)
            tvResults.text = getString(R.string.printing_sequential_mode)  // Limpiar texto anterior
        }

        // Impresión 1: Custom Tag (se envía primero)
        printCustomTag()

        // Impresión 2: Bitmap (se envía inmediatamente SIN esperar el callback del Custom Tag)
        printBitmap()
    }

    /**
     * Modo 2: Imprime la segunda impresión después que responde el callback de éxito de la primera
     * Espera el callback exitoso del custom tag antes de enviar el bitmap
     */
    private fun printBothWithCallback() {
        if (!validateInputs()) return

        binding.apply {
            progressCircular.visible()
            groupInputs.gone()
            btnRestart.gone()  // Asegurar que restart esté oculto
            iconDescription.setImageResource(R.drawable.point_mainapp_demo_app_black_ic_print)
            tvResults.text = getString(R.string.printing_callback_mode)  // Limpiar texto anterior
        }

        // Impresión 1: Custom Tag
        val content = binding.inputText.text.toString()
        val printPdf417InBoleta = binding.checkboxPrintPdf417Boleta.isChecked

        MPManager.bitmapPrinter.print(
            content,
            lastPaymentMethodSelected,
            printPdf417InBoleta,
        ) { response ->
            response
                .doIfSuccess { customTagResult ->
                    // Callback exitoso del custom tag
                    appendResult("✓ Custom Tag: $customTagResult")

                    // Ahora imprimimos el Bitmap después del éxito del custom tag
                    printBitmapWithResult()
                }
                .doIfError { error ->
                    onPrintError("Custom Tag error: ${error.message.orEmpty()}")
                }
        }
    }

    /**
     * Imprime bitmap en modo sequential
     * Como es la última impresión esperada, marca el proceso como completado cuando termina
     */
    private fun printBitmap() {
        val inputStream = resources.openRawResource(R.raw.point_mainapp_demo_app_ic_datafono)
        BitmapFactory.decodeStream(inputStream)?.run {
            MPManager.bitmapPrinter.print(this) { response ->
                response
                    .doIfSuccess { result ->
                        appendResult("✓ Bitmap: $result")
                        // Marca completado cuando bitmap termina exitosamente
                        onPrintsCompleted()
                    }
                    .doIfError { error ->
                        appendResult("✗ Bitmap error: ${error.message.orEmpty()}")
                        // ✓ CORREGIDO: Ahora maneja el error correctamente
                        onPrintError(error.message.orEmpty())
                    }
            }
        } ?: run {
            // ✓ AGREGADO: Manejo de error si no se puede decodificar el bitmap
            appendResult("✗ Bitmap: Error al decodificar imagen")
            onPrintError("Error al decodificar el bitmap")
        }
    }

    /**
     * Imprime custom tag en modo sequential (sin esperar a que termine para continuar)
     * NOTA: En modo sequential, ambas impresiones se disparan inmediatamente.
     * Si hay error aquí, NO se detiene el proceso porque printBitmap() ya se ejecutó.
     */
    private fun printCustomTag() {
        val content = binding.inputText.text.toString()
        val printPdf417InBoleta = binding.checkboxPrintPdf417Boleta.isChecked

        MPManager.bitmapPrinter.print(
            content,
            lastPaymentMethodSelected,
            printPdf417InBoleta,
        ) { response ->
            response
                .doIfSuccess { result ->
                    appendResult("✓ Custom Tag: $result")
                }
                .doIfError { error ->
                    appendResult("✗ Custom Tag error: ${error.message.orEmpty()}")
                    // ✓ CORREGIDO: Ahora maneja el error correctamente
                    // Nota: No llamamos onPrintError() aquí porque printBitmap()
                    // también está corriendo y él decidirá el estado final
                }
        }
    }

    /**
     * Imprime bitmap y procesa el resultado final (usado en modo callback)
     */
    private fun printBitmapWithResult() {
        val inputStream = resources.openRawResource(R.raw.point_mainapp_demo_app_ic_datafono)
        BitmapFactory.decodeStream(inputStream)?.run {
            MPManager.bitmapPrinter.print(this) { response ->
                response
                    .doIfSuccess { result ->
                        appendResult("✓ Bitmap: $result")
                        onPrintsCompleted()
                    }
                    .doIfError { error ->
                        onPrintError("Bitmap error: ${error.message.orEmpty()}")
                    }
            }
        } ?: run {
            onPrintError("Error al decodificar el bitmap")
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
            // Restaurar el contenido hardcodeado en lugar de limpiarlo
            inputText.setText(HARDCODED_CUSTOM_TAG_CONTENT)
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
        /**
         * Contenido hardcodeado del custom tag para las pruebas de impresión
         * Este contenido utiliza tags especiales del formato de impresión:
         * - {br} = Salto de línea
         * - {center} = Centrar texto
         * - {w}{/w} = Texto en negrita/ancho
         * - {s}{/s} = Texto pequeño
         */
        private const val HARDCODED_CUSTOM_TAG_CONTENT =
            "{br}--------------------------------{br}{center}{w} COMPROBANTE DE ENTREGA{/w}{br}{br}{s} Nro pedido :26649{/s}{br}{s} Tienda: Mall Plaza Vespucio-QA{/s}{br}--------------------------------{br}{s}***ITEM(S) DESPACHO***{/s}{br}{s}SKU / ARTICULO                    CANTIDAD    {/s}{br}{s}----------------------------------------------{/s}{br}{s}4065432630504 / BALON FUTBOL ADIDAS WUCL LGE EHV240424   1{br}{s}ENTREGAR: 06/06/2024{/s}{br}{s}DIRECCION: METROPOLITANA Cerro Navia test 12345  {/s}{br}{s}RECIBE: Diego Diaz{/s}{br}{s}entrega a cliente en horario am{/s}{br}--------------------------------{br}"
    }
}
