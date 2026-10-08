package ru.notesapp.util

import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope

suspend fun fetchAll(
    urls: List<String>,
    fetch: suspend (String) -> String,
): List<Result<String>> {
    return supervisorScope {
        val deferredList = urls.map { url ->
            async {
                runCatching { fetch(url) }
            }
        }
        deferredList.map { it.await() }
    }
}