package com.apkakisan.myapplication.profile

import android.net.Uri
import android.telephony.PhoneNumberUtils
import androidx.lifecycle.viewModelScope
import com.apkakisan.myapplication.BaseViewModel
import com.apkakisan.myapplication.User
import com.apkakisan.myapplication.helpers.LocalStore
import com.apkakisan.myapplication.utils.BuildTypeUtil
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val repository: EditProfileRepository
) : BaseViewModel(repository) {

    private var _uiState = MutableSharedFlow<EditProfileUiState>()
    val uiState: SharedFlow<EditProfileUiState> = _uiState

    val user: User = LocalStore.getUser()!!

    var photo = ""
    var name = ""
    var phone = ""
    var address = ""

    init {
        photo = user.photo ?: ""
        name = user.fullName ?: ""
        user.phoneNumber?.let {
            phone = it.substring(3, it.length)
            phone = PhoneNumberUtils.formatNumber(phone, "US")
        }
        address = user.location ?: ""
    }

    fun validate() = viewModelScope.launch {
        phone = phone.filter { it.isLetterOrDigit() || it.isWhitespace() }
        phone = phone.replace(" ", "")
        when {
            name.isEmpty() -> _uiState.emit(EditProfileUiState.EMPTY_NAME)
            phone.length < 10 -> {
                _uiState.emit(EditProfileUiState.INVALID_PHONE)
                if (phone.isEmpty()) _uiState.emit(EditProfileUiState.EMPTY_PHONE)
            }
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

        if (BuildTypeUtil.isDebug() || BuildTypeUtil.isDebugWithRegistration())
            phone = PhoneNumberUtils.formatNumberToE164(phone, "PK")
        if (BuildTypeUtil.isRahul())
            phone = PhoneNumberUtils.formatNumberToE164(phone, "IN")

        val isUpdated = repository.updateUser(
            user.userId,
            name,
            phone,
            address
        )
        if (isUpdated)
            _uiState.emit(EditProfileUiState.PROFILE_UPDATE_SUCCESS)
        else
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
//    object PhotoUploading : EditProfileUiState()
//    object PhotoUploadSuccess : EditProfileUiState()
//    object PhotoUploadFailed : EditProfileUiState()
//    object EmptyName : EditProfileUiState()
//    object InvalidPhone : EditProfileUiState()
//    object EmptyPhone : EditProfileUiState()
//    object EmptyAddress : EditProfileUiState()
//    object DataValidated : EditProfileUiState()
//    object ProfileUpdating : EditProfileUiState()
//    object ProfileUpdateSuccess : EditProfileUiState()
//    object ProfileUpdateFailed : EditProfileUiState()
}