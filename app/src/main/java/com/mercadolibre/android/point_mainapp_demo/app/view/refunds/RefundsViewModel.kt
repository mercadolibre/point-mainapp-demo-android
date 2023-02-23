package com.mercadolibre.android.point_mainapp_demo.app.view.refunds

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadolibre.android.point_mainapp_demo.app.model.RefundsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class RefundsViewModel : ViewModel() {

    private val _result = MutableLiveData<String>()
    val result: LiveData<String> get() = _result

    fun performRefund(paymentId: Long, accessToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            RefundsManager().refundPayment(paymentId, accessToken.appendBearer())
                .catch { _result.value = it.message ?: "Unknown error" }
                .collect {
                    _result.postValue(it.body()?.paymentId?.toString() ?: it.errorBody().toString())
                }
        }
    }

    private fun String.appendBearer(): String {
        if (this.contains(BEARER_SUFFIX).not()) {
            return BEARER_SUFFIX + this
        }
        return this
    }

    companion object {
        const val BEARER_SUFFIX = "Bearer "
    }
}
