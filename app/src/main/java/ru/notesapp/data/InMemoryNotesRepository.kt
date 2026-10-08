package ru.notesapp.data

import ru.notesapp.domain.Note
import ru.notesapp.domain.NoteType

class InMemoryNotesRepository {
    private val notes = mutableListOf(
        Note(
            id = 1L,
            title = "Покупки",
            content = "Купить молоко, хлеб и сыр",
            createdAt = System.currentTimeMillis(),
            type = NoteType.Text
        ),
        Note(
            id = 2L,
            title = "Фото с прогулки",
            content = "Фотография из парка",
            createdAt = System.currentTimeMillis(),
            type = NoteType.Image
        ),
        Note(
            id = 3L,
            title = "Идея для проекта",
            content = "Сделать приложение для заметок на Kotlin",
            createdAt = System.currentTimeMillis(),
            type = NoteType.Text
        ),
        Note(
            id = 4L,
            title = "Голосовая заметка",
            content = "Запись идеи для нового проекта",
            createdAt = System.currentTimeMillis(),
            type = NoteType.Audio
        ),
        Note(
            id = 5L,
            title = "Учёба",
            content = "Повторить Compose и подготовиться к защите практики",
            createdAt = System.currentTimeMillis(),
            type = NoteType.Text
        )
    )

    fun getAll(): List<Note> = notes.toList()
    fun getById(id: Long): Note? = notes.find { it.id == id }
    fun add(note: Note): Long {
        val newId = (notes.maxOfOrNull { it.id } ?: 0) + 1
        notes.add(note.copy(id = newId))
        return newId
    }

    fun delete(id: Long): Boolean = notes.removeIf { it.id == id }
    fun search(query: String): List<Note> =
        notes.filter { it.title.contains(query, ignoreCase = true) }
}
