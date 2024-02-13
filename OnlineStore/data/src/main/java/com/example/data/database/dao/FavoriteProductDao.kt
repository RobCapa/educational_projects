package com.example.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.database.contracts.FavoriteProductDbContract
import com.example.data.models.DFavoriteProduct

@Dao
interface FavoriteProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoriteProducts(items: List<DFavoriteProduct>)

    @Delete
    suspend fun removeFavoriteProducts(items: List<DFavoriteProduct>)

    @Query("SELECT * FROM ${FavoriteProductDbContract.TABLE_NAME}")
    suspend fun getAllFavoriteProducts(): List<DFavoriteProduct>

}