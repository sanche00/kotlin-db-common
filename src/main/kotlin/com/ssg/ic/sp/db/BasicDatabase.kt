package com.ssg.ic.sp.db

import javax.sql.DataSource

interface BasicDatabase<T : Any> {

    var readDataBase: T
    var writeDataBase: T
    val databaseConnection: (datasource: DataSource) -> T

}