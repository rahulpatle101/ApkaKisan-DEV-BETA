package com.apkakisan.myapplication.customerservice

import android.telephony.PhoneNumberUtils
import androidx.lifecycle.viewModelScope
import com.apkakisan.myapplication.BaseViewModel
import com.apkakisan.myapplication.helpers.LocalStore
import com.apkakisan.myapplication.network.requests.CustomerQuery
import com.apkakisan.myapplication.utils.BuildTypeUtil
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.util.*

class CustomerServiceViewModel(
    private val repository: CustomerServiceRepository
) : BaseViewModel(repository) {

    private var _uiState = MutableSharedFlow<CustomerServiceUiState>()
    val uiState: SharedFlow<CustomerServiceUiState> = _uiState

    val user = LocalStore.user

    var nameValue = ""
    var phoneValue = ""
    var messageValue = ""

    init {
        nameValue = user?.fullName ?: ""
        user?.phoneNumber?.let {
            phoneValue = it.substring(3, it.length)
            phoneValue = PhoneNumberUtils.formatNumber(phoneValue, "US")
        }
    }

    fun validate() = viewModelScope.launch {
        phoneValue = phoneValue.filter { it.isLetterOrDigit() || it.isWhitespace() }
        phoneValue = phoneValue.replace(" ", "")
        when {
            nameValue.isEmpty() -> _uiState.emit(CustomerServiceUiState.EmptyName)
            phoneValue.length < 10 -> {
                _uiState.emit(CustomerServiceUiState.InvalidPhone)
                if (phoneValue.isEmpty()) _uiState.emit(CustomerServiceUiState.EmptyPhone)
            }
            messageValue.isEmpty() -> _uiState.emit(CustomerServiceUiState.EmptyMessage)
            else -> _uiState.emit(CustomerServiceUiState.DataValidated)
        }
    }

    fun sendMessage() = viewModelScope.launch {
        _uiState.emit(CustomerServiceUiState.MessageSending)
        val isSent = repository.sendMessage(prepareCustomerQueryRequest())
        if (isSent)
            _uiState.emit(CustomerServiceUiState.MessageSendSuccess)
        else
            _uiState.emit(CustomerServiceUiState.MessageSendFailed)
    }

    private fun prepareCustomerQueryRequest() = CustomerQuery().apply {
        queryId = UUID.randomUUID().toString()
        name = nameValue
        if (BuildTypeUtil.isDebug() || BuildTypeUtil.isDebugWithRegistration())
            phoneValue = PhoneNumberUtils.formatNumberToE164(phoneValue, "PK")
        if (BuildTypeUtil.isRahul())
            phoneValue = PhoneNumberUtils.formatNumberToE164(phoneValue, "IN")
        phoneNo = phoneValue
        message = messageValue
    }
}

sealed class CustomerServiceUiState {
    object EmptyName : CustomerServiceUiState()
    object InvalidPhone : CustomerServiceUiState()
    object EmptyPhone : CustomerServiceUiState()
    object EmptyMessage : CustomerServiceUiState()
    object DataValidated : CustomerServiceUiState()
    object MessageSending : CustomerServiceUiState()
    object MessageSendSuccess : CustomerServiceUiState()
    object MessageSendFailed : CustomerServiceUiState()
}