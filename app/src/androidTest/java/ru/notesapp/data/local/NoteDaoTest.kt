package ru.notesapp.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.notesapp.domain.NoteType

/**
 * Тесты DAO на in-memory Room: база создаётся в памяти устройства
 * и уничтожается после каждого теста.
 */
@RunWith(AndroidJUnit4::class)
class NoteDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: NoteDao

    @get:Rule
    val migrationHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java
    )

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.noteDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    private fun entity(
        id: Long = 0,
        title: String,
        createdAt: Long,
        type: NoteType = NoteType.Text
    ) = NoteEntity(
        id = id,
        title = title,
        content = "content of $title",
        createdAt = createdAt,
        type = type
    )

    @Test
    fun insert_and_observeAll_sortedByCreatedAtDesc() = runBlocking {
        dao.insert(entity(title = "first", createdAt = 100L))
        dao.insert(entity(title = "second", createdAt = 300L))
        dao.insert(entity(title = "third", createdAt = 200L))

        val all = dao.observeAll().first()

        assertEquals(listOf("second", "third", "first"), all.map { it.title })
    }

    @Test
    fun getById_returnsNote_andNullForUnknownId() = runBlocking {
        val id = dao.insert(entity(title = "target", createdAt = 1L))

        assertEquals("target", dao.getById(id)?.title)
        assertNull(dao.getById(999L))
    }

    @Test
    fun searchByTitle_filtersBySubstring() = runBlocking {
        dao.insert(entity(title = "Покупки", createdAt = 1L))
        dao.insert(entity(title = "Идея для проекта", createdAt = 2L))

        val results = dao.searchByTitle("проект").first()

        assertEquals(1, results.size)
        assertEquals("Идея для проекта", results.first().title)
    }

    @Test
    fun update_changesRow() = runBlocking {
        val id = dao.insert(entity(title = "old", createdAt = 1L))

        dao.update(dao.getById(id)!!.copy(title = "new"))

        assertEquals("new", dao.getById(id)?.title)
    }

    @Test
    fun deleteById_removesRow() = runBlocking {
        val id = dao.insert(entity(title = "doomed", createdAt = 1L))

        dao.deleteById(id)

        assertTrue(dao.observeAll().first().isEmpty())
        assertNull(dao.getById(id))
    }

    @Test
    fun typeConverter_roundTrip() = runBlocking {
        dao.insert(entity(title = "audio note", createdAt = 1L, type = NoteType.Audio))
        dao.insert(entity(title = "image note", createdAt = 2L, type = NoteType.Image))

        val types = dao.observeAll().first().associate { it.title to it.type }

        assertEquals(NoteType.Audio, types["audio note"])
        assertEquals(NoteType.Image, types["image note"])
    }

    @Test
    fun migration1To2_addsUpdatedAtColumn() {
        val dbName = "migration-test.db"

        migrationHelper.createDatabase(dbName, 1).apply {
            execSQL(
                "INSERT INTO notes (id, title, content, created_at, type) " +
                    "VALUES (1, 'старая заметка', 'текст', 100, 'TEXT')"
            )
            close()
        }

        migrationHelper.runMigrationsAndValidate(dbName, 2, true, MIGRATION_1_2).apply {
            query("SELECT title, updated_at FROM notes WHERE id = 1").use { cursor ->
                assertTrue(cursor.moveToFirst())
                assertEquals("старая заметка", cursor.getString(0))
                assertEquals(0L, cursor.getLong(1))
            }
            close()
        }
    }

    @Test
    fun migration2To3_addsImageUrlColumn() {
        val dbName = "migration-2-3-test.db"

        migrationHelper.createDatabase(dbName, 2).apply {
            execSQL(
                "INSERT INTO notes (id, title, content, created_at, updated_at, type) " +
                    "VALUES (1, 'заметка без картинки', 'текст', 100, 0, 'TEXT')"
            )
            close()
        }

        migrationHelper.runMigrationsAndValidate(dbName, 3, true, MIGRATION_2_3).apply {
            query("SELECT title, image_url FROM notes WHERE id = 1").use { cursor ->
                assertTrue(cursor.moveToFirst())
                assertEquals("заметка без картинки", cursor.getString(0))
                assertTrue(cursor.isNull(1))
            }
            close()
        }
    }
}
