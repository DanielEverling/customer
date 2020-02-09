package com.customer.controller

import com.customer.commands.CreateCustomerCommand
import org.hamcrest.core.StringContains
import org.junit.Assert.*
import org.junit.Test
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

class CustomerControllerIT {

    private val resource = "http://api:8080/api/customer"

    @Test
    fun `should create customer` () {
        val createCustomerCommand = CreateCustomerCommand(fullName = "Renato Portallupi", nickName = "renato", email = "renato@gremio.net")

        val response = RestTemplate().postForEntity(resource, createCustomerCommand, CreateCustomerCommand::class.java)

        val createCustomerCommandReceived = response.body
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("Renato Portallupi", createCustomerCommandReceived?.fullName)
        assertEquals("renato", createCustomerCommandReceived?.nickName)
        assertNotNull(createCustomerCommandReceived?.createdAt)
        assertNotNull(createCustomerCommandReceived?.id)
    }

    @Test
    fun `should validate customer creation with full name and empty name`() {
        val createCustomerCommand = CreateCustomerCommand(fullName = "", nickName = "", email = "")

        try {
            RestTemplate().postForEntity(resource, createCustomerCommand, String::class.java)
        } catch (e: HttpClientErrorException) {
            assertEquals(HttpStatus.BAD_REQUEST, e.statusCode)
            val body = e.responseBodyAsString
            assertThat(body, StringContains.containsString("Complete Name is required."))
            assertThat(body, StringContains.containsString("Nick Name is required."))
            assertThat(body, StringContains.containsString("Email is required."))
        }
    }

    @Test
    fun `should validate the creation of the customer with the full name of the nick name greater than the maximum size`() {
        val createCustomerCommand = CreateCustomerCommand(fullName = "Silvio Santos Ipsum ma oi.", nickName = "silviosantosipsummaoi.", email = "silvio@sbt.com")

        try {
            RestTemplate().postForEntity(resource, createCustomerCommand, String::class.java)
        } catch (e: HttpClientErrorException) {
            assertEquals(HttpStatus.BAD_REQUEST, e.statusCode)
            val body = e.responseBodyAsString
            assertThat(body, StringContains.containsString("Complete Name must be less than 20 characters."))
            assertThat(body, StringContains.containsString("Nick Name must be less than 7 characters."))
        }
    }

}