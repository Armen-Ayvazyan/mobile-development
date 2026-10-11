package ru.notesapp.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.notesapp.data.local.NoteDao
import ru.notesapp.data.remote.NoteApi
import ru.notesapp.data.remote.NetworkResult
import ru.notesapp.data.remote.safeApiCall
import ru.notesapp.data.remote.toDto
import ru.notesapp.data.remote.toEntity
import ru.notesapp.data.local.toDomain
import ru.notesapp.domain.Note

class SyncedNotesRepository(
    private val dao: NoteDao,
    private val api: NoteApi,
) {
    fun observeAll(): Flow<List<Note>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    suspend fun refresh(): NetworkResult<Int> = safeApiCall {
        val posts = api.listPosts()
        val entities = posts.map { it.toEntity() }
        dao.insertAll(entities)
        posts.size
    }

    suspend fun add(note: Note): NetworkResult<Long> = safeApiCall {
        val dto = note.toDto()
        val created = api.createPost(dto)
        val entity =
            created.toEntity().copy(type = note.type, title = note.title, content = note.content)
        dao.insert(entity)
    }

    suspend fun delete(id: Long): NetworkResult<Unit> = safeApiCall {
        api.deletePost(id)
        dao.deleteById(id)
    }
}