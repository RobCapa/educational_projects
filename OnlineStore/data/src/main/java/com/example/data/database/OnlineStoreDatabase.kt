package com.example.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.database.OnlineStoreDatabase.Companion.DB_VERSION
import com.example.data.database.dao.FavoriteProductDao
import com.example.data.database.dao.UserDao
import com.example.data.models.DFavoriteProduct
import com.example.data.models.DUser

@Database(
    entities = [
        DUser::class,
        DFavoriteProduct::class,
    ],
    version = DB_VERSION,
)
abstract class OnlineStoreDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun favoriteProductDao(): FavoriteProductDao

    companion object {
        const val DB_VERSION = 2
        const val DB_NAME = "OnlineStoreDatabase"
    }
}