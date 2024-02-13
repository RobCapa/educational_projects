package com.example.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.database.contracts.FavoriteProductDbContract

@Entity(tableName = FavoriteProductDbContract.TABLE_NAME)
data class DFavoriteProduct(
    @PrimaryKey
    @ColumnInfo(name = FavoriteProductDbContract.COLUMNS.PRODUCT_ID)
    val productId: String,
)