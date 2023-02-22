package com.mercadolibre.android.point_mainapp_demo.app.view.refunds

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadolibre.android.point_mainapp_demo.app.model.RefundsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class RefundsViewModel : ViewModel() {
    fun performRefund(paymentId: Long, amount: Double, accessToken: String) {
        Log.i(TAG, "Refund payment --> $paymentId, amount: $amount, AT: $accessToken")
        viewModelScope.launch(Dispatchers.IO) {
            RefundsManager().refundPayment(paymentId, amount, accessToken)
                .catch { Log.e("Refunds", it.message.toString()) }
                .collect {
                    Log.d("Refunds", it.body()?.paymentId?.toString() ?: it.raw().toString())
                }
        }
    }

    companion object {
        const val TAG = "RefundsFlow"
    }
}
