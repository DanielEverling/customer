package com.cross.domain

import java.util.*

sealed class ResultEntity<out L, out R> {
    class Failure(val notifications: List<Notification>) : ResultEntity<List<Notification>, Nothing>()
    class Success<T : Entity>(val success: T) : ResultEntity<Nothing, T>()
}

open abstract class Entity {

    protected abstract fun validate() : List<Optional<Notification>>

    protected val notifications : List<Notification>
        get() = validate()
                .filter { it.isPresent }
                .map { it.get() }

    protected fun hasNotification() : Boolean =
            validate()
            .filter { it.isPresent }
            .isNotEmpty()

}