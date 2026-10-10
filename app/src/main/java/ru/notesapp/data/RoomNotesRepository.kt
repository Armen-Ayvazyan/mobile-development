package ru.notesapp.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.notesapp.data.local.NoteDao
import ru.notesapp.data.local.toDomain
import ru.notesapp.data.local.toEntity
import ru.notesapp.domain.Note

class RoomNotesRepository(private val dao: NoteDao) : NotesRepository {
    override fun observeAll(): Flow<List<Note>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun getById(id: Long): Note? =
        dao.getById(id)?.toDomain()

    override fun searchByTitle(query: String): Flow<List<Note>> =
        dao.searchByTitle(query).map { list -> list.map { it.toDomain() } }

    override suspend fun add(note: Note): Long =
        dao.insert(note.toEntity())

    override suspend fun update(note: Note) =
        dao.update(note.toEntity())

    override suspend fun delete(id: Long) {
        dao.deleteById(id)
    }
}