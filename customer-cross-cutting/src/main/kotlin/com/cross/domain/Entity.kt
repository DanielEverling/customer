package com.cross.domain

import java.util.*

sealed class ResultEntity<out L, out R> {
    class Failure(val notifications: List<Notification>) : ResultEntity<List<Notification>, Nothing>()
    class Success<T : Entity>(val success: T) : ResultEntity<Nothing, T>()
}

open abstract class Entity : ValidatorsAware {

    protected fun validate(): List<Notification> {
        return validators()
                .filter { it.isPresent }
                .map { it.get() }
    }

    protected fun hasError(): Boolean = validate().isNotEmpty()

}