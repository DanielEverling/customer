package com.customer.domain


import com.cross.domain.ResultEntity
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class CustomerTest {

    private val customerRepository = mock(CustomerRepository::class.java)
    private val customerSpecification = CustomerSpecification(customerRepository)

    @Before
    fun setup() {
        `when`(customerRepository.countByCompleteNameAndNickNameAndDifferentId(anyString(), anyString(), anyLong())).thenReturn(0)
    }

    @Test
    fun `should create customer`() {
        val resultCustomer = Customer.create(fullName = "Ranato Portaluppi", nickName = "renato", email = Email("renato@imortal.com"), customerSpecification = customerSpecification)

       when(resultCustomer) {
           is ResultEntity.Success ->
               apply {
                   val customer = resultCustomer.success
                   assertEquals("Ranato Portaluppi", customer.fullName)
                   assertEquals("renato", customer.nickName)
                   assertEquals( Email("renato@imortal.com"), customer.email)
                   assertEquals(CustomerStatus.ACTIVE, customer.status)

               }
           is ResultEntity.Failure -> fail("Result Costumer should be success")
       }
    }

    @Test
    fun  `should validate customer creation with full name and empty name`() {

        val numbers = mutableListOf("one", "two", "three", "four", "five")
        val resultList = numbers.map { it.length }.filter { it > 3 }
        println(resultList)

        val resultCustomer = Customer.create(fullName = "", nickName = "", email = Email(""), customerSpecification = customerSpecification)

        when(resultCustomer) {
            is ResultEntity.Failure ->
                apply {
                    assertTrue("Should have notifications", resultCustomer.notifications.isNotEmpty())
                    val notifications = resultCustomer.notifications
                    assertEquals("Complete Name", notifications[0].field)
                    assertEquals("Complete Name is required.", notifications[0].notification)
                    assertEquals("Nick Name", notifications[1].field)
                    assertEquals("Nick Name is required.", notifications[1].notification)
                    assertEquals("Email is required.", notifications[2].notification)
                }
        }
    }

    @Test
    fun `should validate the creation of the customer with the full name of the nick name greater than the maximum size`() {
        val resultCustomer = Customer.create(fullName = "Silvio Santos Ipsum ma oi.", nickName = "silviosantosipsummaoi", email = Email("silvio@sbt.com"), customerSpecification = customerSpecification)

        when(resultCustomer) {
            is ResultEntity.Failure ->
                apply {
                    assertTrue("Should have notifications", resultCustomer.notifications.isNotEmpty())
                    val notifications = resultCustomer.notifications
                    assertEquals("Complete Name must be less than 20 characters.", notifications[0].notification)
                    assertEquals("Nick Name must be less than 7 characters.", notifications[1].notification)
                }
        }
    }

    @Test
    fun `should validate creation customer when already exists`() {
        `when`(customerRepository.countByCompleteNameAndNickNameAndDifferentId(anyString(), anyString(), anyLong())).thenReturn(1)
        val resultCustomer = Customer.create(fullName = "Ranato Portaluppi", nickName = "renato", email = Email("renato@imortal.com"), customerSpecification = customerSpecification)

        when(resultCustomer) {
            is ResultEntity.Failure ->
                apply {
                    assertTrue("Should have notifications", resultCustomer.notifications.isNotEmpty())
                    val notifications = resultCustomer.notifications
                    assertEquals("Customer already exists.", notifications[0].notification)
                }
        }
    }

}