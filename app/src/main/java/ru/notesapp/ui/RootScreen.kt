package ru.notesapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavGraph.Companion.findStartDestination
import ru.notesapp.ui.navigation.Routes
import ru.notesapp.ui.theme.ThemeVariant

private data class BottomItem(
    val route: String,
    val title: String,
    val icon: ImageVector
)

@Composable
fun RootScreen() {

    val navController = rememberNavController()

    var currentTheme by remember {
        mutableStateOf(ThemeVariant.PASTEL)
    }

    val bottomItems = listOf(
        BottomItem(
            route = Routes.NOTES,
            title = "Заметки",
            icon = Icons.Default.Note
        ),
        BottomItem(
            route = Routes.SETTINGS,
            title = "Настройки",
            icon = Icons.Default.Settings
        )
    )

    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute = currentBackStackEntry
        ?.destination
        ?.route

    Scaffold(
        bottomBar = {

            if (currentRoute == Routes.NOTES ||
                currentRoute == Routes.SETTINGS
            ) {

                NavigationBar {

                    bottomItems.forEach { item ->

                        NavigationBarItem(
                            selected = currentRoute == item.route,

                            onClick = {

                                navController.navigate(item.route) {

                                    popUpTo(
                                        navController.graph.findStartDestination().id
                                    ) {
                                        saveState = true
                                    }

                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },

                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title
                                )
                            },

                            label = {
                                Text(item.title)
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Routes.NOTES,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(Routes.NOTES) {

                NotesListScreen(
                    onNoteClick = { note ->
                        navController.navigate(
                            Routes.note(note.id)
                        )
                    }
                )
            }

            composable(
                route = Routes.NOTE,
                arguments = listOf(
                    navArgument("id") {
                        type = NavType.LongType
                    }
                )
            ) { entry ->

                val id = entry.arguments
                    ?.getLong("id")
                    ?: -1L

                NoteDetailScreen(
                    noteId = id,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Routes.SETTINGS) {

                SettingsScreen(
                    currentTheme = currentTheme,

                    onThemeChange = { theme ->
                        currentTheme = theme
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RootScreenPreview() {
    RootScreen()
}