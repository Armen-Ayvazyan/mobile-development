package ru.notesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ru.notesapp.ui.RootScreen
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
                RootScreen()
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
        RootScreen()
    }
}

@Preview(name = "Pastel Dark", showBackground = true)
@Composable
private fun PastelDarkPreview() {
    NotesAppTheme(
        darkTheme = true,
        themeVariant = ThemeVariant.PASTEL
    ) {
        RootScreen()
    }
}

@Preview(name = "Forest Light", showBackground = true)
@Composable
private fun ForestLightPreview() {
    NotesAppTheme(
        darkTheme = false,
        themeVariant = ThemeVariant.FOREST
    ) {
        RootScreen()
    }
}

@Preview(name = "Forest Dark", showBackground = true)
@Composable
private fun ForestDarkPreview() {
    NotesAppTheme(
        darkTheme = true,
        themeVariant = ThemeVariant.FOREST
    ) {
        RootScreen()
    }
}