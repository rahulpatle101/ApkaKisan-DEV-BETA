package com.apkakisan.myapplication.profile

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.apkakisan.myapplication.BaseViewModel
import com.apkakisan.myapplication.User
import com.apkakisan.myapplication.helpers.LocalStore
import com.apkakisan.myapplication.utils.PhoneNoUtil
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val repository: EditProfileRepository
) : BaseViewModel(repository) {

    private var _uiState = MutableSharedFlow<EditProfileUiState>()
    val uiState: SharedFlow<EditProfileUiState> = _uiState

    val user: User = LocalStore.getUser() ?: User()

    var photo = ""
    var name = ""
    var phone = ""
    var address = ""

    init {
        photo = user.photo ?: ""
        name = user.fullName ?: ""
        user.phoneNumber?.let {
            phone = it.substring(3, it.length)
            phone = PhoneNoUtil.format10DigitToUS(phone)
        }
        address = user.location ?: ""
    }

    fun formatUSTo10Digit() {
        phone = PhoneNoUtil.formatUSTo10Digit(phone)
    }

    fun validate() = viewModelScope.launch {
        when {
            name.isEmpty() -> _uiState.emit(EditProfileUiState.EMPTY_NAME)
            phone.isEmpty() -> _uiState.emit(EditProfileUiState.EMPTY_PHONE)
            (phone.length < 10 || phone.length > 10) -> _uiState.emit(EditProfileUiState.INVALID_PHONE)
            address.isEmpty() -> _uiState.emit(EditProfileUiState.EMPTY_ADDRESS)
            else -> _uiState.emit(EditProfileUiState.DATA_VALIDATED)
        }
    }

    fun uploadPhoto(photoUri: Uri) = viewModelScope.launch {
        _uiState.emit(EditProfileUiState.PHOTO_UPLOADING)
        val isPhotoUpdated = repository.uploadPhoto(user.userId, photoUri)
        if (isPhotoUpdated) {
            photo = LocalStore.getUser()?.photo!!
            _uiState.emit(EditProfileUiState.PHOTO_UPLOAD_SUCCESS)
        } else {
            _uiState.emit(EditProfileUiState.PHOTO_UPLOAD_FAILED)
        }
    }

    fun updateUser() = viewModelScope.launch {
        _uiState.emit(EditProfileUiState.PROFILE_UPDATING)
        phone = PhoneNoUtil.formatForServer(phone)
        val isUpdated = repository.updateUser(
            user.userId,
            name,
            phone,
            address
        )
        if (isUpdated) {
            _uiState.emit(EditProfileUiState.PROFILE_UPDATE_SUCCESS)
            LocalStore.getUser()?.apply {
                fullName = name
                phoneNumber = phone
                location = address
            }?.also {
                it.save()
            }
        } else
            _uiState.emit(EditProfileUiState.PROFILE_UPDATE_FAILED)
    }
}

enum class EditProfileUiState {
    PHOTO_UPLOADING,
    PHOTO_UPLOAD_SUCCESS,
    PHOTO_UPLOAD_FAILED,
    EMPTY_NAME,
    INVALID_PHONE,
    EMPTY_PHONE,
    EMPTY_ADDRESS,
    DATA_VALIDATED,
    PROFILE_UPDATING,
    PROFILE_UPDATE_SUCCESS,
    PROFILE_UPDATE_FAILED
}