package ru.notesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import ru.notesapp.data.RoomNotesRepository
import ru.notesapp.data.local.AppDatabase
import ru.notesapp.ui.RootScreen
import ru.notesapp.ui.theme.NotesAppTheme
import ru.notesapp.ui.theme.ThemeVariant
import ru.notesapp.viewmodel.NotesViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: NotesViewModel by viewModels {
        val database = AppDatabase.getInstance(applicationContext)
        val repository = RoomNotesRepository(database.noteDao())
        NotesViewModel.Factory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesAppTheme(
                themeVariant = ThemeVariant.PASTEL
            ) {
                RootScreen(viewModel = viewModel)
            }
        }
    }
}