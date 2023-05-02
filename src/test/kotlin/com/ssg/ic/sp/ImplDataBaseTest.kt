package com.ssg.ic.sp

import com.ssg.ic.sp.db.DBConnect
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class ImplDataBaseTest {

    @Test
    fun exposedTest() {

        val dbConnect = DBConnect(url = "jdbc:h2:mem:test", driver = "org.h2.Driver", user = "sa")
        val database = dbConnect.database { Database.connect(it) }
        transaction(database) {
            // print sql to std-out
            addLogger(StdOutSqlLogger)

            SchemaUtils.create(Cities)

            // insert new city. SQL: INSERT INTO Cities (name) VALUES ('St. Petersburg')
//        val stPeteId = Cities.insert {
//            it[name] = "St. Petersburg"
//        } get Cities.id

            Cities.insert {
                it[id1] = 1
                it[id2] = 1
                it[name] = "St. Petersburg"
            }
            // 'select *' SQL: SELECT Cities.id, Cities.name FROM Cities
            Cities.select {
                Cities.id2 eq 1
            }.forEach {
                Assertions.assertEquals(it[Cities.id1], 1)
                Assertions.assertEquals(it[Cities.id2], 1)
                Assertions.assertEquals(it[Cities.name], "St. Petersburg")
            }
        }
    }
}

object Cities : Table("tba_city") {
    val id1 = integer("id1")
    val id2 = integer("id2")

    val name = varchar("name", 50)

    override val primaryKey = PrimaryKey(id1, id2)
}