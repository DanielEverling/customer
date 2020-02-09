package com.customer.domain

import com.cross.domain.Notification
import com.cross.domain.ValueObject
import com.cross.extensions.isNullOrBlank
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

data class Email (val email: String) : ValueObject() {

    init {
        validate()
    }

    override fun validate(): List<Optional<Notification>> = listOf(
            email.isNullOrBlank("Email is required."),
            emailIsValid()
    )

    private fun emailIsValid() : Optional<Notification> {
        val pattern: Pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        val matcher: Matcher = pattern.matcher(email)
        return when(matcher.matches()) {
            true -> Optional.empty()
            else -> Optional.of(Notification(notification = "Email should be valid."))
        }
    }

}