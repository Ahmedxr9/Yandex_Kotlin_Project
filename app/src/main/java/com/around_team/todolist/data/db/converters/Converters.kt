package com.around_team.todolist.data.db.converters

import androidx.room.TypeConverter

/**
 * Type converters for complex types used in the Room database.
 *
 * This implementation provides explicit conversion between a list of file paths
 * and a single string column to avoid schema changes while keeping the entity simple.
 */
class Converters {

    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        // Use a simple join representation; existing schema already stores files as TEXT.
        return value?.joinToString(separator = "|")
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        if (value.isNullOrEmpty()) return null
        return value.split("|").map { it }.filter { it.isNotEmpty() }
    }
}
