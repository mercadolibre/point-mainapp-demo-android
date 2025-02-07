package com.mercadolibre.android.point_mainapp_demo.app.view.payment.models

import com.mercadolibre.android.point_integration_sdk.nativesdk.payment.data.PayerCondition
import com.mercadolibre.android.point_integration_sdk.nativesdk.payment.data.Tax

internal typealias PayerConditionString = String

internal fun PayerConditionString.toTaxes() = PayerCondition.fromString(this)
    ?.let { payerCondition ->
        listOf(Tax(payerCondition))
    }
