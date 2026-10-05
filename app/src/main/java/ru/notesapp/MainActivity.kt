package ru.notesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ru.notesapp.ui.NotesListScreen
import ru.notesapp.ui.theme.NotesAppTheme
import ru.notesapp.ui.theme.ThemeVariant

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NotesAppTheme(
                themeVariant = ThemeVariant.PASTEL,
                darkTheme = true
            ) {
                NotesListScreen()
            }
        }
    }
}

@Preview(name = "Pastel Light", showBackground = true)
@Composable
private fun PastelLightPreview() {
    NotesAppTheme(
        darkTheme = false,
        themeVariant = ThemeVariant.PASTEL
    ) {
        NotesListScreen()
    }
}

@Preview(name = "Pastel Dark", showBackground = true)
@Composable
private fun PastelDarkPreview() {
    NotesAppTheme(
        darkTheme = true,
        themeVariant = ThemeVariant.PASTEL
    ) {
        NotesListScreen()
    }
}

@Preview(name = "Forest Light", showBackground = true)
@Composable
private fun ForestLightPreview() {
    NotesAppTheme(
        darkTheme = false,
        themeVariant = ThemeVariant.FOREST
    ) {
        NotesListScreen()
    }
}

@Preview(name = "Forest Dark", showBackground = true)
@Composable
private fun ForestDarkPreview() {
    NotesAppTheme(
        darkTheme = true,
        themeVariant = ThemeVariant.FOREST
    ) {
        NotesListScreen()
    }
}