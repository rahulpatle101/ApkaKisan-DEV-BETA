package com.apkakisan.myapplication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.apkakisan.myapplication.User

@Dao
interface UserDao {
    @Insert
    fun addUser(user: User)

    @Query("SELECT * FROM user")
    fun getUser(): User
}