package com.cross.domain

import java.util.*

interface Specification<T> {

    fun isSatisfiedBy(entity : T) : Optional<Notification>

}