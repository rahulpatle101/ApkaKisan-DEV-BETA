package com.apkakisan.myapplication.database

import com.apkakisan.myapplication.User

class RoomDataSource(private val userDao: UserDao) {
    fun getUser() = userDao.getUser()
    fun addUser(user: User) = userDao.addUser(user)
}