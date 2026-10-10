package ru.notesapp.data

import kotlinx.coroutines.flow.Flow
import ru.notesapp.domain.Note

interface NotesRepository {
    fun observeAll(): Flow<List<Note>>
    suspend fun getById(id: Long): Note?
    fun searchByTitle(query: String): Flow<List<Note>>
    suspend fun add(note: Note): Long
    suspend fun update(note: Note)
    suspend fun delete(id: Long)
}