package ru.notesapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.notesapp.data.NotesRepository
import ru.notesapp.data.remote.NetworkResult
import ru.notesapp.domain.Note
import ru.notesapp.domain.NoteType
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repository: NotesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<NotesUiState>(NotesUiState.Loading)
    val uiState: StateFlow<NotesUiState> = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _uiEvent = MutableSharedFlow<String>()
    val uiEvent: SharedFlow<String> = _uiEvent.asSharedFlow()

    init {
        observeNotes()
    }

    private fun observeNotes() {
        viewModelScope.launch {
            _uiState.value = NotesUiState.Loading
            try {
                repository.observeAll().collect { notes ->
                    _uiState.value = if (notes.isEmpty()) {
                        NotesUiState.Empty
                    } else {
                        NotesUiState.Success(notes)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = NotesUiState.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true

            when (val result = repository.refresh()) {
                is NetworkResult.Success -> {
                    _uiEvent.emit("Обновлено: ${result.data} заметок")
                }

                is NetworkResult.Error -> {
                    _uiEvent.emit("Ошибка сервера: ${result.code}")
                }

                is NetworkResult.NetworkError -> {
                    _uiEvent.emit("Нет соединения с интернетом")
                }
            }

            _isRefreshing.value = false
        }
    }

    fun addNote(title: String, content: String, type: NoteType) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val newNote = Note(
                    id = 0L,
                    title = title,
                    content = content,
                    createdAt = System.currentTimeMillis(),
                    type = type,
                    imageUrl = null
                )
                repository.add(newNote)
            }
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.delete(id)
            }
        }
    }
}