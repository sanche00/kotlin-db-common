package com.ssg.ic.sp.db

import com.ssg.ic.sp.db.model.Entity
import java.util.stream.Stream
import kotlin.streams.toList

interface Repository<T : Entity<ID>, ID> {

    fun findById(id: ID): T?

    fun findAll(): Stream<T>

    fun deleteById(id: ID): Int

    fun delete(t: T): Int {
        return deleteById(t.id)
    }

    fun deleteAll(list: List<T>): Int {
        return list.stream().mapToInt { delete(it) }.sum()
    }

    fun save(t: T): T

    fun saveAll(list: List<T>): List<T> {
        return saveAll(list.stream()).toList()
    }

    fun saveAll(stream: Stream<T>): Stream<T> {
        return stream.map { save(it) }
    }

}