package com.mercadolibre.android.point_mainapp_demo.app.data.dto

import com.google.gson.annotations.SerializedName

data class RefundResponse(val id: Long?, @SerializedName("payment_id") val paymentId: Long?)
