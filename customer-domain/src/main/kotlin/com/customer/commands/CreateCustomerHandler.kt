package com.customer.commands

import com.cross.commands.BaseCommandHandler
import com.cross.domain.ResultEntity.*
import com.customer.domain.Customer
import com.customer.domain.CustomerRepository
import com.cross.events.DomainInvalidEvent
import com.customer.domain.CustomerSpecification
import com.customer.domain.Email
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(propagation = Propagation.REQUIRED)
class CreateCustomerHandler(val customerSpecification: CustomerSpecification, val customerRepository: CustomerRepository) : BaseCommandHandler() {

    fun handle(customerCommand: CreateCustomerCommand) {

        logger.toField("complete name", customerCommand.fullName)
              .toField("nick name", customerCommand.nickName)
              .withInfoMessage("Received command with parameters")


        val resultCustomer = Customer.build(customerSpecification) {
            email = Email(email = customerCommand.email)
            fullName = customerCommand.fullName
            nickName = customerCommand.nickName
        }

        when(resultCustomer) {
            is Failure -> eventPublisher.publisher(DomainInvalidEvent(resultCustomer.notifications))
            is Success -> customerRepository.insert(resultCustomer.success)
        }

    }

}