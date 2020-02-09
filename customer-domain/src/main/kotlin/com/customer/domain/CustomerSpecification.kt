package com.customer.domain

import com.cross.domain.Notification
import com.cross.domain.Specification
import org.springframework.stereotype.Component
import java.util.*

@Component
class CustomerSpecification (private val repository: CustomerRepository) : Specification<Customer> {
    override fun isSatisfiedBy(entity: Customer): Optional<Notification> {
        return when(repository.countByCompleteNameAndNickNameAndDifferentId(entity.fullName, entity.nickName, entity.id)) {
            0 -> Optional.empty()
            else -> Optional.of(Notification(notification = "Customer already exists."))
        }
    }
}