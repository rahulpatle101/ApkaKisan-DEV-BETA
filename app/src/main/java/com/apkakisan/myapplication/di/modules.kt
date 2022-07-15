package com.apkakisan.myapplication.di

import android.app.Application
import androidx.room.Room
import com.apkakisan.myapplication.customerservice.CustomerServiceRepository
import com.apkakisan.myapplication.customerservice.CustomerServiceViewModel
import com.apkakisan.myapplication.database.AppDatabase
import com.apkakisan.myapplication.database.RoomDataSource
import com.apkakisan.myapplication.database.UserDao
import com.apkakisan.myapplication.network.FirebaseDataSource
import com.apkakisan.myapplication.notifications.NotificationRepository
import com.apkakisan.myapplication.notifications.NotificationSettingRepository
import com.apkakisan.myapplication.notifications.NotificationSettingViewModel
import com.apkakisan.myapplication.notifications.NotificationViewModel
import com.apkakisan.myapplication.profile.EditProfileRepository
import com.apkakisan.myapplication.profile.EditProfileViewModel
import com.apkakisan.myapplication.profile.ProfileRepository
import com.apkakisan.myapplication.profile.ProfileViewModel
import com.apkakisan.myapplication.registration.VerifyPhoneNoRepository
import com.apkakisan.myapplication.registration.VerifyPhoneNoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun providesDatabase(application: Application): AppDatabase =
    Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "apkakisan"
    ).fallbackToDestructiveMigration().build()

fun providesDao(db: AppDatabase): UserDao = db.userDao()

val appModule = module {

    single { FirebaseDataSource() }
    single { providesDatabase(get()) }
    single { providesDao(get()) }
    single { RoomDataSource(get()) }

    single { VerifyPhoneNoRepository(get()) }
    single { NotificationRepository(get()) }
    single { NotificationSettingRepository(get()) }
    single { ProfileRepository(get()) }
    single { EditProfileRepository(get()) }
    single { CustomerServiceRepository(get()) }

    viewModel { VerifyPhoneNoViewModel(get()) }
    viewModel { NotificationViewModel(get()) }
    viewModel { NotificationSettingViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { EditProfileViewModel(get()) }
    viewModel { CustomerServiceViewModel(get()) }
}