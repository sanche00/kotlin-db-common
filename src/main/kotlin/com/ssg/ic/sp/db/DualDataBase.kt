package com.ssg.ic.sp.db

import org.slf4j.LoggerFactory
import javax.sql.DataSource

class DualDataBase<T : Any>(
    private val writeConnect: DBConnect,
    private val readConnect: DBConnect? = null,
    val databaseConnection: (datasource: DataSource) -> T
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    var readDataBase: T
        private set
    var writeDataBase: T
        private set

    init {
        writeDataBase = writeConnect.database(databaseConnection)
        readDataBase = readConnect?.database(databaseConnection) ?: writeDataBase
    }

}