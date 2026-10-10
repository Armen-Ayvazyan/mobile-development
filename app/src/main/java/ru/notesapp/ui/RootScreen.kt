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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.notesapp.ui.navigation.Routes
import ru.notesapp.ui.theme.ThemeVariant
import ru.notesapp.viewmodel.NotesViewModel

private data class BottomItem(
    val route: String,
    val title: String,
    val icon: ImageVector
)

private val bottomItems = listOf(
    BottomItem(Routes.NOTES, "Заметки", Icons.Default.Note),
    BottomItem(Routes.SETTINGS, "Настройки", Icons.Default.Settings)
)

@Composable
fun RootScreen(
    viewModel: NotesViewModel,
    onThemeChange: (ThemeVariant) -> Unit = {},
    currentTheme: ThemeVariant = ThemeVariant.PASTEL
) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            // Нижняя панель навигации отображается только на главных экранах (notes и settings)[cite: 58, 59]
            if (currentRoute in bottomItems.map { it.route }) {
                NavigationBar {
                    bottomItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title) }
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
            // Экран списка заметок[cite: 58]
            composable(Routes.NOTES) {
                NotesListScreen(
                    viewModel = viewModel,
                    onNoteClick = { note ->
                        navController.navigate(Routes.note(note.id))
                    }
                )
            }

            // Экран подробной информации о заметке[cite: 58]
            composable(
                route = Routes.NOTE,
                arguments = listOf(navArgument("id") { type = NavType.LongType })
            ) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getLong("id") ?: -1L
                NoteDetailScreen(
                    noteId = noteId,
                    onBack = { navController.popBackStack() }
                )
            }

            // Экран настроек приложения[cite: 58]
            composable(Routes.SETTINGS) {
                SettingsScreen(
                    onThemeChange = onThemeChange,
                    currentTheme = currentTheme
                )
            }
        }
    }
}