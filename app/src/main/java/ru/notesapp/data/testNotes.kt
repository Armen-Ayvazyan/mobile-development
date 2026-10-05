package ru.notesapp.data

import ru.notesapp.domain.Note
import ru.notesapp.domain.NoteType

val notes = listOf(
    Note(
        id = 1L,
        title = "Покупки",
        content = "Купить молоко, хлеб и сыр",
        createdAt = System.currentTimeMillis(),
        type = NoteType.Text
    ),
    Note(
        id = 2L,
        title = "Фото с прогулки",
        content = "Фотография из парка",
        createdAt = System.currentTimeMillis(),
        type = NoteType.Image
    ),
    Note(
        id = 3L,
        title = "Идея для проекта",
        content = "Сделать приложение для заметок на Kotlin",
        createdAt = System.currentTimeMillis(),
        type = NoteType.Text
    ),
    Note(
        id = 4L,
        title = "Голосовая заметка",
        content = "Запись идеи для нового проекта",
        createdAt = System.currentTimeMillis(),
        type = NoteType.Audio
    ),
    Note(
        id = 5L,
        title = "Учёба",
        content = "Повторить Compose и подготовиться к защите практики",
        createdAt = System.currentTimeMillis(),
        type = NoteType.Text
    )
)