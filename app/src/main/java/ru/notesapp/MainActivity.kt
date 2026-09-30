package ru.notesapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.notesapp.domain.Note
import ru.notesapp.domain.NoteType
import ru.notesapp.domain.summary
import ru.notesapp.ui.NoteList
import ru.notesapp.ui.theme.NotesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val notes = listOf(
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

        setContent {
            NotesAppTheme {
                Scaffold { innerPadding ->
                    NoteList(
                        modifier = Modifier.padding(innerPadding),
                        notes = notes,
                        onClick = { note ->
                            Log.d("NotesApp", "Clicked: ${note.id}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting() {
    val notes = listOf(
        Note(
            id = 1,
            title = "Хлеб",
            content = "Текст хлеб",
            createdAt = System.currentTimeMillis(),
            type = NoteType.Text
        ),
        Note(
            id = 2,
            title = "Хлеб",
            content = "Картинка хлеб",
            createdAt = System.currentTimeMillis(),
            type = NoteType.Image
        ),
        Note(
            id = 3,
            title = "Хлеб",
            content = "Аудио хлеб",
            createdAt = System.currentTimeMillis(),
            type = NoteType.Audio
        ),
    )
    notes.forEach {
        Log.d("HER", "HER")
        Log.d("NotesApp", it.summary())
    }

}