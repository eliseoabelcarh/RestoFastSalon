package com.restofast.salon.sharedData.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class UserRoom(
    @PrimaryKey @ColumnInfo(name = "uid") var uid: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "lastname") var lastname: String,
    @ColumnInfo(name = "dni") var dni: String,
    @ColumnInfo(name = "email") var email: String,
    @ColumnInfo(name = "password") var password: String,

    )