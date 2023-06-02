package com.ssg.ic.sp

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class FlowTest {

    @Test
    fun main() = runBlocking<Unit> {
        (1..3).asFlow().flatMapMerge { requestFlow(it) }.onEach { println(it) }.toList()
    }

    fun requestFlow(i: Int): Flow<String> = flow {
        val time = 1000L/i
        delay(time)
        emit("$i: First")
    }

}