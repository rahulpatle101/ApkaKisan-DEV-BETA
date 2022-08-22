package com.apkakisan.myapplication.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apkakisan.myapplication.helpers.LocalStore
import com.apkakisan.myapplication.network.responses.Notification
import kotlinx.coroutines.launch

class NotificationViewModel(private val repository: NotificationRepository) : ViewModel() {

    private val _notificationList = MutableLiveData<List<Notification>>()
    val notificationList: LiveData<List<Notification>> = _notificationList

    fun getNotifications() {
        viewModelScope.launch {
            _notificationList.postValue(repository.getNotifications(LocalStore.getUser()?.userId!!))
        }
    }
}