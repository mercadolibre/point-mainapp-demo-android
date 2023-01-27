package com.mercadolibre.android.point_mainapp_demo.app.data

import com.google.gson.annotations.SerializedName

enum class PaymentMethod {
    @SerializedName("point")
    POINT,

    @SerializedName("credit")
    CREDIT,

    @SerializedName("debit")
    DEBIT,

    @SerializedName("qr")
    QR,

    @SerializedName("voucher")
    VOUCHER,

    @SerializedName("link", alternate = ["link_payment"])
    LINK,

    @SerializedName("undefined")
    UNDEFINED
}
