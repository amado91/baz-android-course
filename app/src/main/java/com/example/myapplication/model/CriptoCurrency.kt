package com.example.myapplication.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by: Juan Antonio Amado
 * date: 24,septiembre,2022
 */
@Entity(tableName = "currency")
data class CriptoCurrency(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "price") var price: String,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)

data class BitsoCustom(val name: Int, val icon: Int, val idBook: String)