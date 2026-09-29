package ru.notesapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import ru.notesapp.domain.Note
import ru.notesapp.domain.NoteType
import ru.notesapp.domain.summary
import ru.notesapp.ui.theme.NotesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesAppTheme {
                Greeting()
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