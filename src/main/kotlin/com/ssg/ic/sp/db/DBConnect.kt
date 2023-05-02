package com.ssg.ic.sp.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.util.Properties
import javax.sql.DataSource

const val ENV_DB_DRIVER = "db.driver"
const val ENV_DB_WRITE_URL = "db.url"
const val ENV_DB_WRITE_USER = "db.user"
const val ENV_DB_WRITE_PWD = "db.pwd"

const val DEFAULT_POOL_SIZE = 3

class DBConnect(
    private val url: String = System.getenv(ENV_DB_WRITE_URL),
    private val driver: String = System.getenv(ENV_DB_DRIVER),
    private val user: String = System.getenv(ENV_DB_WRITE_USER)?:"",
    private val pwd: String = System.getenv(ENV_DB_WRITE_PWD)?:"",
    private val dbProperties: Properties = System.getProperties()
) {

    private var dataSource: DataSource? = null

    private fun connection(): DataSource {
        if (dataSource == null) {
            val config = HikariConfig().apply {
                driverClassName = driver
                jdbcUrl = url
                maximumPoolSize = (dbProperties?.getProperty("db.pool.size") ?: DEFAULT_POOL_SIZE.toString()).toInt()
                username = user
                password = pwd
//            isAutoCommit = (dbProperties?.getProperty("db.autocommit")?: "false").toBoolean()
                isAutoCommit = false
                addDataSourceProperty("cachePrepStmts", "true")
                addDataSourceProperty("prepStmtCacheSize", "250")
                addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
                validate()
            }
            dataSource = HikariDataSource(config)
        }
        return dataSource!!
    }

    fun <T> database(databaseConnection: (datasource: DataSource)-> T):T {
        return databaseConnection(connection())
    }
}