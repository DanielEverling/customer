package com.customer.domain

import com.cross.domain.Notification
import org.junit.Assert.*
import org.junit.Test

class EmailTest {

    @Test
    fun `should create e-mail`() {
        val email = Email("test@test.com")
        assertTrue("list of notification should be empty", email.validate().filter { it.isPresent }.map { it }.isEmpty())
        assertEquals("test@test.com", email.email)
    }

    @Test
    fun `should validate creation email with empty value`() {
        val email = Email("")
        assertTrue("list of notification should not be empty", email.validate().filter { it.isPresent }.map { it }.isNotEmpty())
        val expectedNotification = Notification(notification = "Email is required.")
        val receivedNotification = email.validate().filter { it.isPresent }.map { it.get() }.map { it }.first()
        assertEquals(expectedNotification, receivedNotification)
    }

    @Test
    fun `should validate creation e-mail`() {
        val email = Email("test.com")
        assertTrue("list of notification should not be empty", email.validate().filter { it.isPresent }.map { it }.isNotEmpty())
        val expectedNotification = Notification(notification = "Email should be valid.")
        val receivedNotification = email.validate().filter { it.isPresent }.map { it.get() }.map { it }.first()
        assertEquals(expectedNotification, receivedNotification)
    }

}