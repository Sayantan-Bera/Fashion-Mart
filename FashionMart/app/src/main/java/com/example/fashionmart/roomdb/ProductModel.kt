package com.example.fashionmart.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.annotation.Nonnull

@Entity(tableName = "Cart")
data class ProductModel(
    @PrimaryKey
    @Nonnull
    val productId:String,
    @ColumnInfo(name = "productName")
    val productName:String?="",
    @ColumnInfo(name = "productImage")
    val productImage:String?="",
    @ColumnInfo(name = "productSp")
    val productSp:String?=""
)
