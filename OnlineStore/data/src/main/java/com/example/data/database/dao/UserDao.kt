package com.example.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.database.contracts.UserDbContract
import com.example.data.models.DUser

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: DUser)

    @Delete
    suspend fun removeUser(user: DUser)

    @Query(
        "SELECT * FROM ${UserDbContract.TABLE_NAME} " +
                "WHERE ${UserDbContract.COLUMNS.ID} = :id"
    )
    suspend fun getUser(id: String): DUser?
}