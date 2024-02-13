package com.example.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.database.contracts.UserDbContract

@Entity(tableName = UserDbContract.TABLE_NAME)
data class DUser(
    @PrimaryKey
    @ColumnInfo(name = UserDbContract.COLUMNS.ID)
    val id: String,
    @ColumnInfo(name = UserDbContract.COLUMNS.FIRST_NAME)
    val firstName: String,
    @ColumnInfo(name = UserDbContract.COLUMNS.LAST_NAME)
    val lastName: String,
    @ColumnInfo(name = UserDbContract.COLUMNS.PHONE_NUMBER)
    val phoneNumber: String,
)
