package com.cross.extensions

import com.cross.domain.Notification
import java.util.*

fun String.validateSizeSmallerThan(length: Int, message: String, field : String? = null) : Optional<Notification> {
    return when {
        this.length > length -> Optional.of(Notification(notification = message, field = field))
        else -> Optional.empty()
    }
}

fun String.isNullOrBlank(message: String, field : String? = null) : Optional<Notification> {
   return when {
       this.isNullOrBlank() -> Optional.of(Notification(notification = message, field = field))
       else -> Optional.empty()
    }
}