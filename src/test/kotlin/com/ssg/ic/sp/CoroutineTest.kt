package com.ssg.ic.sp

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.lang.Thread.sleep

fun main() {
    println("Start Main Thread")
    GlobalScope.launch() {
        delay(3000)
        println("in Coroutine ...")
    }
    println("End Main Thread")
    sleep(4000)
}
class CoroutineTest {

    @Test
    fun test1() {
        runBlocking{
            print("Start Main Thread")
            GlobalScope.launch {
                delay(3000)
                print("in Coroutine ...")
            }
            print("End Main Thread")
        }

    }


}