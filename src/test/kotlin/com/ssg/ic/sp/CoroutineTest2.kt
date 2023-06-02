package com.ssg.ic.sp

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.asFlow
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.lang.Thread.sleep

class CoroutineTest2 {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Test
    fun test1() {
        runBlocking {
            GlobalScope.launch {
                launch {
                    print("At Here!")
                }
                val value: Int = async {
                    var total = 0
                    for (i in 1..10) total += i
                    total
                }.await()
                print("$value")
            }
        }
    }


    @Test
    fun test2() {
        runBlocking {
            GlobalScope.launch {
                val x = doSomething()
                println("done something, $x")
            }
        }
    }

    private suspend fun doSomething(): Int {
        val value: Int = GlobalScope.async(Dispatchers.IO) {
            var total = 0
            for (i in 1..10) total += i
            println("do something in a suspend method: $total")
            total
        }.await()
        return value
    }

    @Test
    fun test3() {

        println("Start...")
        runBlocking {
            GlobalScope.launch {
                val job1 = async(Dispatchers.IO) {
                    println("${Thread.currentThread().name}")
                    var total = 0
                    for (i in 1..10) {
                        total += i
                        delay(100)
                        log.info("job1 ${total}")
                    }
                    println("job1")
                    total
                }
                val job2 = async(Dispatchers.IO) {
                    println("${Thread.currentThread().name}")
                    var total = 0
                    for (i in 1..10) {
                        delay(100)
                        total += i
                        log.info("job2 ${total}")
                    }
                    log.info("job2")
                    total
                }
                val result1 = job1.await()
                val result2 = job2.await()
                println("results are $result1 and $result2")
            }
        }
        println("End.")

        sleep(2000)
    }

    @Test
    fun test4() {
        runBlocking {
            GlobalScope.launch(Dispatchers.IO) {
                println("${Thread.currentThread().name}")
                val v = withContext(Dispatchers.Default) {
                    println("${Thread.currentThread().name}")
                    var total = 0
                    for (i in 1..10) {
                        delay(100)
                        total += i
                    }
                    total
                }
                println("result: $v")
                println("Do something in IO thread")
            }
        }
        sleep(2000)
    }

    @Test
    fun test5() {
        println("start..")
        val job = GlobalScope.launch() {
            repeat(10) {
                delay(1000L)
                println("I'm working.")
            }
        }
        runBlocking {
            delay(3000L)
            job.cancel()
        }
        println("stop")
        sleep(4000L)
    }

    @Test
    fun test6() {
        println("start..")
        val job = GlobalScope.launch() {
            repeat(10) {
                delay(1000L)
                println("I'm working.")
            }
        }
        runBlocking {
            job.join()
        }
        println("stop")
        sleep(11000)
    }

    @Test
    fun test7() {
        println("start..")
        val job = GlobalScope.launch {
            withTimeout(4000L) {
                repeat(10) {
                    delay(1000L)
                    println("I'm working.")
                }
            }
        }


        println("stop")
        sleep(5000)
    }

    @Test
    fun test8() {
        runBlocking {
            println("start")
            val channel = Channel<Int>()
            launch {
                for (x in 1..5) {
                    channel.send(x * x)
                }
            }
            repeat(5) {
                val v = channel.receive()
                println("$v")
            }
            println("end")
        }
    }

    @Test
    fun test9() {
        runBlocking {
            val channel = Channel<String>()
            launch {
                sendString(channel, "foo", 200L)
            }
            launch {
                sendString(channel, "BAR", 500L)
            }
            repeat(6) {
                print("${channel.receive()}")
            }
            coroutineContext.cancelChildren()
        }
    }

    private suspend fun sendString(channel: SendChannel<String>, s: String, time: Long) {
        while (true) {
            delay(time)
            channel.send(s)
        }
    }

    suspend fun requestFlow(i: Int) = (1..3).onEach { delay(100) }.map { "$i : $it" }.onEach { log.info(it) }


    val startTime = System.currentTimeMillis()

    @Test
    fun main1() = runBlocking<Unit> {


//        GlobalScope.launch {
            val b = (1..3).onEach { delay(100) }.map { i ->
                val ret = async(Dispatchers.IO) {
                    requestFlow(i)
                }
                ret
            }
            b.awaitAll()
                .forEach { value -> log.info("$value at ${System.currentTimeMillis() - startTime} ms from start") }
//        }

        sleep(2000)
//        job.join()
//        (1..3).asFlow().onEach { delay(100) }.flatMapMerge {
//            async (Dispatchers.IO) {
//                delay(100)
//                val ret = requestFlow(it)
//                log.info("$ret")
//                ret
//            }.await()
//        }.collect { value ->
//            log.info("$value at ${System.currentTimeMillis() - startTime} ms from start")
//        }
//        (1..3).asFlow().flowOn(Dispatchers.IO).onEach { delay(100) }
//            .flatMapMerge {
//                async {
//                    val ret = requestFlow(it)
//                    log.info("$ret")
//                    delay(100)
//                    ret
//                }
//            }
//            .collect { value ->
//                log.info("$value at ${System.currentTimeMillis() - startTime} ms from start")
//            }

    }
}