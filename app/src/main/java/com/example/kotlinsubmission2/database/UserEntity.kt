package com.example.kotlinsubmission2.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.kotlinsubmission2.helper.Helper

@Entity(tableName = Helper.TABLE_NAME)
data class UserEntity(
    @PrimaryKey
    var id: Int = 0,
    var login: String = "",
    var avatarUrl: String = ""
)