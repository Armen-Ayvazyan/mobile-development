package ru.notesapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.notesapp.data.local_notes
import ru.notesapp.domain.Note
import ru.notesapp.domain.NoteType
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    noteId: Long,
    onBack: () -> Unit
) {
    val note = local_notes.find { it.id == noteId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Заметка #$noteId")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        if (note == null) {
            EmptyState(
                title = "Заметка не найдена",
                subtitle = "Заметка с таким id отсутствует",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        } else {
            NoteDetailContent(
                note = note,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }

    }
}


@Composable
private fun NoteDetailContent(
    note: Note,
    modifier: Modifier = Modifier,
) {
    val dateFormatter = SimpleDateFormat(
        "dd.MM.yyyy HH:mm",
        LocalLocale.current.platformLocale
    )

    val typeName = when (note.type) {
        NoteType.Text -> "ТЕКСТ"
        NoteType.Image -> "КАРТИНКА"
        NoteType.Audio -> "АУДИО"
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = note.title,
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = typeName,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = dateFormatter.format(Date(note.createdAt)),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )

        Spacer(modifier = Modifier.padding(4.dp))

        Text(
            text = note.content,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NoteDetailScreenPreview() {
    NoteDetailScreen(noteId = 1L, onBack = {})
}

@Preview(showBackground = true)
@Composable
private fun NoteDetailScreenNotFoundPreview() {
    NoteDetailScreen(noteId = 99L, onBack = {})
}