package ru.notesapp.domain

fun Note.summary(maxChars: Int = 50): String{
    val newString = buildString {
        append("[")
        append(when(type){
            NoteType.Text -> "T"
            NoteType.Image -> "I"
            NoteType.Audio -> "A"
        })
        append("] ")
        append(if (content.length <= maxChars) content else content.take(maxChars) + "...")
    }

    return newString
}