package com.ssg.ic.sp.db.model

abstract class Entity<ID> {
    open var id: ID? = null
        get() {
            return field
        }
        set(value) {
            value?.let { divideId(it) }
            field = value
        }

    abstract fun setId()

    abstract fun divideId(id: ID)
    fun init() {
        setId()
    }
}