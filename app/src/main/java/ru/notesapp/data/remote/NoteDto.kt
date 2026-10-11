package ru.notesapp.data.remote

import com.squareup.moshi.JsonClass
import ru.notesapp.data.local.NoteEntity
import ru.notesapp.domain.Note
import ru.notesapp.domain.NoteType

@JsonClass(generateAdapter = true)
data class NoteDto(
    val id: Long?,
    val title: String?,
    val body: String?,
    val userId: Long?,
)

fun NoteDto.toEntity(): NoteEntity = NoteEntity(
    id = id ?: 0L,
    title = title.orEmpty(),
    content = body.orEmpty(),
    createdAt = System.currentTimeMillis(),
    updatedAt = System.currentTimeMillis(),
    type = NoteType.Text,
    imageUrl = null
)

fun Note.toDto(): NoteDto = NoteDto(
    id = if (id == 0L) null else id,
    title = title,
    body = content,
    userId = 1L
)