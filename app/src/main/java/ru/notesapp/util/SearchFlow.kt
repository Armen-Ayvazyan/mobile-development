package ru.notesapp.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

@OptIn(FlowPreview::class)
fun <T> searchFlow(
    queries: Flow<String>,
    minLength: Int = 2,
    debounceMs: Long = 300,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    search: suspend (String) -> List<T>
): Flow<List<T>> {
    return queries
        .debounce(debounceMs)
        .distinctUntilChanged()
        .filter { it.length >= minLength || it.isEmpty() }
        .map { query ->
            if (query.isEmpty()) {
                emptyList()
            } else {
                search(query)
            }
        }
        .catch { emit(emptyList()) }
        .flowOn(dispatcher)

}
