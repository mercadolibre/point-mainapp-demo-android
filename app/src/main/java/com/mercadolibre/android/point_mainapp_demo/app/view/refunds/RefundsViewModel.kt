package com.mercadolibre.android.point_mainapp_demo.app.view.refunds

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RefundsViewModel : ViewModel() {
    fun performRefund(paymentId: Long, amount: Double, accessToken: String) {
        Log.i(TAG, "Refund payment --> $paymentId, amount: $amount")
        viewModelScope.launch(Dispatchers.IO) {

        }
    }

    companion object {
        const val TAG = "RefundsFlow"
    }
}
