package ru.notesapp

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.notesapp.util.countdown
import ru.notesapp.util.fetchAll
import ru.notesapp.util.searchFlow
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class CoroutinesTest {

    // 1
    @Test
    fun test_countdown_emits_from_to_zero() = runTest {
        val result = countdown(3, 1000).toList()
        assertEquals(listOf(3, 2, 1, 0), result)
    }

    // 2
    @Test
    fun test_countdown_completes_after_zero() = runTest {
        val emissions = mutableListOf<Int>()
        countdown(2, 500).collect { emissions.add(it) }
        assertEquals(3, emissions.size)
        assertEquals(0, emissions.last())
    }

    // 3
    @Test
    fun test_countdown_cancellable() = runTest {
        val emissions = mutableListOf<Int>()
        val job = launch {
            countdown(5, 1000).collect { emissions.add(it) }
        }
        advanceTimeBy(1500.milliseconds)
        job.cancel()
        advanceTimeBy(3000)
        assertEquals(listOf(5, 4), emissions)
    }

    // 4
    @Test
    fun test_fetchAll_parallel_returns_all_results() = runTest {
        val urls = listOf("url1", "url2", "url3")
        val results = fetchAll(urls) { url -> "Data: $url" }

        assertEquals(3, results.size)
        assertTrue(results.all { it.isSuccess })
        assertEquals("Data: url1", results[0].getOrNull())
    }

    // 5
    @Test
    fun test_fetchAll_one_fails_others_succeed() = runTest {
        val urls = listOf("ok1", "fail", "ok2")
        val results = fetchAll(urls) { url ->
            if (url == "fail") throw RuntimeException("Network Error")
            "Data: $url"
        }

        assertEquals(3, results.size)
        assertTrue(results[0].isSuccess)
        assertTrue(results[1].isFailure)
        assertTrue(results[2].isSuccess)
    }

    // 6
    @Test
    fun test_searchFlow_debounce_skips_intermediate() = runTest {
        val queries = MutableStateFlow("")
        val searchCalls = mutableListOf<String>()
        val results = mutableListOf<List<String>>()

        val job = launch {
            searchFlow(
                queries,
                minLength = 2,
                debounceMs = 300,
                dispatcher = StandardTestDispatcher(testScheduler)
            ) { q ->
                searchCalls.add(q)
                listOf("Result for $q")
            }.collect { results.add(it) }
        }

        queries.value = "п"
        advanceTimeBy(100)
        queries.value = "пр"
        advanceTimeBy(100)
        queries.value = "при"
        advanceTimeBy(400) // Ждём больше debounceMs

        job.cancel()

        assertEquals(listOf("при"), searchCalls)
    }

    // 7
    @Test
    fun test_searchFlow_empty_query_returns_empty_list() = runTest {
        val queries = MutableStateFlow("")
        var searchCalled = false

        val job = launch {
            searchFlow(queries, minLength = 2, debounceMs = 300) {
                searchCalled = true
                listOf("data")
            }.collect {}
        }

        advanceTimeBy(400)
        job.cancel()

        assertEquals(false, searchCalled)
    }

    // 8
    @Test
    fun test_searchFlow_min_length_filter() = runTest {
        val queries = MutableStateFlow("")
        var searchCalled = false

        val job = launch {
            searchFlow(queries, minLength = 3, debounceMs = 300) {
                searchCalled = true
                listOf("data")
            }.collect {}
        }

        queries.value = "a"
        advanceTimeBy(400)
        job.cancel()

        assertEquals(false, searchCalled)
    }

    // 9
    @Test
    fun test_searchFlow_catch_emits_empty_on_error() = runTest {
        val queries = MutableStateFlow("test")

        val result = searchFlow<String>(
            queries,
            minLength = 2,
            debounceMs = 300,
            dispatcher = StandardTestDispatcher(testScheduler)
        ) {
            throw RuntimeException("Search failed")
        }

        println(result)

        val emissions = mutableListOf<List<String>>()
        val job = launch { result.collect { emissions.add(it) } }

        advanceTimeBy(400)
        job.cancel()

        assertEquals(1, emissions.size)
        assertTrue(emissions.first().isEmpty())
    }
}