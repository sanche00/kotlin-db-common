package com.ssg.ic.sp.db

interface ORM<T : Any>{
    val database: BasicDatabase<T>

    fun readDatabase():T
    fun writeDatabase():T

    fun <V> read(block: (database: T) -> V): V
    fun <V> write(block: (database: T) -> V): V

    suspend fun <T> writeExec(
        block: () -> T
    ): T

    suspend fun <T> readExec(
        block: () -> T
    ): T

    fun nextval(sequence: String): Long

}
