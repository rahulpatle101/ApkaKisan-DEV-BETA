package com.apkakisan.myapplication

import androidx.lifecycle.ViewModel

abstract class BaseViewModel(
    private val repository: BaseRepository
) : ViewModel()