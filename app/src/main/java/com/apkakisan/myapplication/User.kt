package com.apkakisan.myapplication

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.apkakisan.myapplication.helpers.LocalStore
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user")
data class User(
    @PrimaryKey var userId: String = "",
    @ColumnInfo(name = "fullName") var fullName: String? = null,
    @ColumnInfo(name = "location") var location: String? = null,
    @ColumnInfo(name = "createdDate") var createdDate: String? = null,
    @ColumnInfo(name = "modifiedDate") var modifiedDate: String? = null,
    @ColumnInfo(name = "phoneNumber") var phoneNumber: String? = null,
    @ColumnInfo(name = "pinCode") var pinCode: String? = null,
    @ColumnInfo(name = "photo") var photo: String? = null
) : Parcelable {
    fun save() {
        LocalStore.setUser(this)
    }
}
