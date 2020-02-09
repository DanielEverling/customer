package com.cross.domain

import java.util.*

open abstract class ValueObject {

    abstract fun validate() : List<Optional<Notification>>

}