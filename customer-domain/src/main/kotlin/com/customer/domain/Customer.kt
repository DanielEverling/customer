package com.customer.domain

import com.cross.domain.*
import com.cross.domain.ResultEntity.*
import com.cross.extensions.isNullOrBlank
import com.cross.extensions.validateSizeSmallerThan
import java.util.*

enum class CustomerStatus {
    INACTIVE, ACTIVE
}

data class Customer private constructor(
        val id : Long?,
        val fullName : String,
        val nickName : String,
        val email: Email,
        val customerSpecification : CustomerSpecification) : Entity () {

    private val _status: CustomerStatus = CustomerStatus.ACTIVE

    val status
        get() = _status

    init {
        validate()
    }

    companion object {
        inline fun build(customerSpecification : CustomerSpecification, block: Builder.() -> Unit) = Builder(customerSpecification).apply(block).build()
    }

    override fun validate(): List<Optional<Notification>> = listOf(
            fullName.validateSizeSmallerThan(20, "Complete Name must be less than 20 characters.", "Complete Name"),
            fullName.isNullOrBlank(message = "Complete Name is required.", field = "Complete Name"),
            nickName.isNullOrBlank("Nick Name is required.", "Nick Name"),
            nickName.validateSizeSmallerThan(7, "Nick Name must be less than 7 characters.", "Nick Name"),
            customerSpecification.isSatisfiedBy(this)
    ).plus(
            email.validate()
    )

    class Builder (private val customerSpecification : CustomerSpecification) {
        var id : Long? = null
        var fullName : String = ""
        var nickName : String = ""
        var email: Email = Email("")

        fun build() : ResultEntity<List<Notification>, Customer> {
            val newCustomer = Customer(id, fullName, nickName, email, customerSpecification)

            return  when(newCustomer.hasNotification()) {
                true -> Failure(newCustomer.notifications)
                else -> Success<Customer>(newCustomer)
            }
        }
    }
}
