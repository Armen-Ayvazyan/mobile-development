package ru.notesapp.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.notesapp.data.local.NoteDao
import ru.notesapp.data.local.toDomain
import ru.notesapp.data.remote.NetworkResult
import ru.notesapp.data.remote.NoteApi
import ru.notesapp.data.remote.safeApiCall
import ru.notesapp.data.remote.toDto
import ru.notesapp.data.remote.toEntity
import ru.notesapp.domain.Note
import javax.inject.Inject

class SyncedNotesRepository @Inject constructor(
    private val dao: NoteDao,
    private val api: NoteApi,
) : NotesRepository {

    override fun observeAll(): Flow<List<Note>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun refresh(): NetworkResult<Int> = safeApiCall {
        val posts = api.listPosts()
        val entities = posts.map { it.toEntity() }
        dao.insertAll(entities)
        posts.size
    }

    override suspend fun add(note: Note): NetworkResult<Long> = safeApiCall {
        val dto = note.toDto()
        val created = api.createPost(dto)
        val entity = created.toEntity().copy(
            type = note.type,
            title = note.title,
            content = note.content,
            imageUrl = note.imageUrl
        )
        dao.insert(entity)
    }

    override suspend fun delete(id: Long): NetworkResult<Unit> = safeApiCall {
        api.deletePost(id)
        dao.deleteById(id)
    }

    override suspend fun getById(id: Long): Note? = dao.getById(id)?.toDomain()

    override fun search(query: String): Flow<List<Note>> =
        dao.searchByTitle(query).map { list -> list.map { it.toDomain() } }
}
