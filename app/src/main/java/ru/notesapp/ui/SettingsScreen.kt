package ru.notesapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.notesapp.ui.theme.ThemeVariant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onThemeChange: (ThemeVariant) -> Unit,
    currentTheme: ThemeVariant
) {
    var darkTheme by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Настройки")
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Тема приложения",
                style = MaterialTheme.typography.titleMedium
            )

            ThemeRadioButton(
                title = "Pastel",
                selected = currentTheme == ThemeVariant.PASTEL,
                onClick = {
                    onThemeChange(ThemeVariant.PASTEL)
                }
            )

            ThemeRadioButton(
                title = "Forest",
                selected = currentTheme == ThemeVariant.FOREST,
                onClick = {
                    onThemeChange(ThemeVariant.FOREST)
                }
            )

            HorizontalDivider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "Тёмная тема",
                    style = MaterialTheme.typography.bodyLarge
                )

                Switch(
                    checked = darkTheme,
                    onCheckedChange = {
                        darkTheme = it
                    }
                )
            }

            HorizontalDivider()

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "О приложении",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "NotesApp v0.4.0 (Практика 4)",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}


@Composable
private fun ThemeRadioButton(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        RadioButton(
            selected = selected,
            onClick = onClick
        )

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    SettingsScreen(
        onThemeChange = {},
        currentTheme = ThemeVariant.PASTEL
    )
}