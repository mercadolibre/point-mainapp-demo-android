package com.mercadolibre.android.point_mainapp_demo.app.model

import com.mercadolibre.android.point_mainapp_demo.app.data.NetworkDependencyProvider
import com.mercadolibre.android.point_mainapp_demo.app.data.RefundsService
import com.mercadolibre.android.point_mainapp_demo.app.data.dto.RefundResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class RefundsManager(private val refundsClient: RefundsService = NetworkDependencyProvider.refundsService) {
    suspend fun refundPayment(paymentId: Long, amount: Double, accessToken: String): Flow<Response<RefundResponse>> {
        return flow {
            emit(refundsClient.createRefund(paymentId.toString(), amount, accessToken))
        }
    }
}
