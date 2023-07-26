package com.asimodabas.haberinolsun.db

import androidx.room.TypeConverter
import com.asimodabas.haberinolsun.domain.model.Source

class Converters {
    @TypeConverter
    fun fromSource(source: Source): String? = source.name

    @TypeConverter
    fun toSource(name: String): Source = Source(name, name)
}