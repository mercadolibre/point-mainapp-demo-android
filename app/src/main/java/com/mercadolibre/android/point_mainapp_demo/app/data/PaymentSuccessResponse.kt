package com.mercadolibre.android.point_mainapp_demo.app.data

import com.google.gson.annotations.SerializedName
import com.mercadolibre.android.point_mainapp_demo.app.util.EMPTY
import java.io.Serializable

data class PaymentSuccessResponse(
    @SerializedName("paymentType", alternate = ["type"])
    val paymentMethod: PaymentMethod = PaymentMethod.UNDEFINED,
    @SerializedName("paymentReference", alternate = ["reference"])
    val paymentReference: String = EMPTY,
    @SerializedName("paymentCreationDate", alternate = ["creation_date"])
    val paymentCreationDate: String = EMPTY,
    @SerializedName("paymentAmount", alternate = ["amount"])
    val paymentAmount: Number = 0,
    @SerializedName("paymentSnDevice", alternate = ["sn_device"])
    val paymentSnDevice: String = EMPTY,
    @SerializedName("paymentInstallments", alternate = ["installments"])
    val paymentInstallments: String = EMPTY,
    @SerializedName("paymentBrandName", alternate = ["brand_name"])
    val paymentBrandName: String = EMPTY,
    @SerializedName("paymentLastFourDigits", alternate = ["last_four_digits"])
    val paymentLastFourDigits: String = EMPTY
) : Serializable
