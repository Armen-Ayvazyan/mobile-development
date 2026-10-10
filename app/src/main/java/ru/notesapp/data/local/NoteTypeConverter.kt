package ru.notesapp.data.local

import androidx.room.TypeConverter
import ru.notesapp.domain.NoteType

class NoteTypeConverter {
    @TypeConverter
    fun fromType(type: NoteType): String {
        return when (type) {
            is NoteType.Text -> "TEXT"
            is NoteType.Image -> "IMAGE"
            is NoteType.Audio -> "AUDIO"
        }
    }

    @TypeConverter
    fun toType(value: String): NoteType {
        return when (value) {
            "TEXT" -> NoteType.Text
            "IMAGE" -> NoteType.Image
            "AUDIO" -> NoteType.Audio
            else -> throw IllegalArgumentException("Unknown NoteType: $value")
        }
    }
}