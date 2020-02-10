package com.cross.domain

import java.util.*

interface ValidatorsAware {
    fun validators() : List<Optional<Notification>>
}
