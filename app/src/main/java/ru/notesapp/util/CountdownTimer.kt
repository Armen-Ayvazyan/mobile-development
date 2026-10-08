package ru.notesapp.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.milliseconds

fun countdown(
    fromSeconds: Int,
    tickMs: Long = 1000
): Flow<Int> {
    return flow {
        for (i in fromSeconds downTo 0) {
            emit(i)
            if (i > 0) {
                delay(tickMs.milliseconds)
            }
        }
    }
}