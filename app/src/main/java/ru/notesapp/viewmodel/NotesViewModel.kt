package ru.notesapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.notesapp.data.InMemoryNotesRepository
import ru.notesapp.domain.Note
import ru.notesapp.domain.NoteType
import kotlin.time.Duration.Companion.milliseconds

class NotesViewModel(
    private val repository: InMemoryNotesRepository = InMemoryNotesRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<NotesUiState>(NotesUiState.Loading)
    val uiState: StateFlow<NotesUiState> = _uiState.asStateFlow()

    init {
        loadNotes()
    }

    fun loadNotes() {
        _uiState.value = NotesUiState.Loading
        viewModelScope.launch {
            try {
                val notes = withContext(Dispatchers.IO) {
                    delay(500.milliseconds)
                    repository.getAll()
                }
                _uiState.value = if (notes.isEmpty()) {
                    NotesUiState.Empty
                } else {
                    NotesUiState.Success(notes)
                }
            } catch (e: Exception) {
                _uiState.value = NotesUiState.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }

    fun addNote(title: String, content: String, type: NoteType) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.add(
                    Note(
                        id = 0,
                        title = title,
                        content = content,
                        createdAt = System.currentTimeMillis(),
                        type = type
                    )
                )
            }
            loadNotes()
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.delete(id)
            }
            loadNotes()
        }
    }

    fun searchNotes(query: String) {
        _uiState.value = NotesUiState.Loading
        viewModelScope.launch {
            val results = withContext(Dispatchers.IO) {
                if (query.isBlank()) {
                    repository.getAll()
                } else {
                    repository.search(query)
                }
            }
            _uiState.value = if (results.isEmpty()) {
                NotesUiState.Empty
            } else {
                NotesUiState.Success(results)
            }
        }
    }
}