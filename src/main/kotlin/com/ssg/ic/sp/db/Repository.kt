package com.ssg.ic.sp.db

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface Repository<DATABASE, TRANSACTION> {

    fun <T> transaction(database: DATABASE? = null, block: (database: DATABASE, transaction: TRANSACTION?) -> T): T
    fun <T> readTransaction(database: DATABASE? = null, block: (database: DATABASE, transaction: TRANSACTION?) -> T): T

    fun writeDatabase(): DATABASE
    fun readDatabase(): DATABASE


    suspend fun <T> writeExec(
        block: (database: DATABASE, transaction: TRANSACTION?) -> T
    ): T = withContext(Dispatchers.IO) {
        transaction(writeDatabase(), block)
    }

    suspend fun <T> readExec(
        block: (database: DATABASE, transaction: TRANSACTION?) -> T
    ): T = withContext(Dispatchers.IO) {
        transaction(readDatabase(), block)
    }

    fun nextval(sequence: String): Long

    fun <T> read(database: DATABASE?, block: (database: DATABASE) -> T) = if (database == null) {
        readTransaction() { db, _ -> block(db) }
    } else {
        block(database)
    }


    fun <T> write(database: DATABASE?, block: (database: DATABASE) -> T) = if (database == null) {
        transaction() { db, _ -> block(db) }
    } else {
        block(database)
    }
}