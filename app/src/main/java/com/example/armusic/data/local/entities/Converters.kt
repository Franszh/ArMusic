package com.example.armusic.data.local.entities


import androidx.room.TypeConverter


class Converters {
    @TypeConverter
    fun fromStringList(list: List<String>?): String{
        return list?.joinToString(separator = ",")?:""
    }

    @TypeConverter
    fun toStringList(data: String?): List<String>{
        return data?.split(",")?.map { it.trim() } ?: emptyList()
    }
}