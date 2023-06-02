package com.ssg.ic.sp.db

import org.slf4j.LoggerFactory
import javax.sql.DataSource

class DualDataBase<T : Any>(
    private val writeConnect: DBConnect,
    private val readConnect: DBConnect? = null,
    override val databaseConnection: (datasource: DataSource) -> T,
) : BasicDatabase<T>{

    private val logger = LoggerFactory.getLogger(this::class.java)

    var ss:T? = null
        private set
    init {
        writeConnect.database(databaseConnection).also { writeDataBase = it }
        (readConnect?.database(databaseConnection) ?: writeDataBase).also { readDataBase = it }
    }

    override var readDataBase: T
    override var writeDataBase: T

}