package com.apkakisan.myapplication.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apkakisan.myapplication.User
import com.apkakisan.myapplication.helpers.LocalStore
import com.apkakisan.myapplication.network.responses.NotificationTypeSetting
import com.apkakisan.myapplication.network.responses.NotificationType
import kotlinx.coroutines.launch

class NotificationSettingViewModel(private val repository: NotificationSettingRepository) :
    ViewModel() {

    private val _notificationSettingList = MutableLiveData<List<NotificationType>>()
    val notificationSettingList: LiveData<List<NotificationType>> = _notificationSettingList

    val user: User = LocalStore.getUser()!!

    fun getNotificationType() {
        viewModelScope.launch {
            _notificationSettingList.postValue(
                repository.getNotificationType(
                    user.userId
                )
            )
        }
    }

    fun setNotificationTypeState(notificationSetting: NotificationTypeSetting) {
        viewModelScope.launch {
            notificationSetting.userId = user.userId
            repository.setNotificationTypeState(notificationSetting)
        }
    }
}