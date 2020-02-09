package com.customer.repository

import com.customer.domain.Customer
import com.customer.domain.CustomerProjection
import com.customer.domain.CustomerRepository
import org.jetbrains.exposed.sql.*
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional


@Repository
@Transactional(propagation = Propagation.MANDATORY)
class CustomerRepositoryImpl : CustomerRepository {

    override fun insert(customer: Customer) {
        CustomerTable.insert {
            it[completeName] = customer.fullName
            it[nickName] = customer.nickName
            it[status] = customer.status.ordinal
        }
    }

    override fun findAll(): List<CustomerProjection> = CustomerTable
            .selectAll()
            .map{
                toCustomerProjection(it)
            }.toList()

    override fun countByCompleteNameAndNickNameAndDifferentId(completeName: String, nickName: String, id : Long?): Int {
        val query = CustomerTable.selectAll()
        id?.let { query.andWhere { CustomerTable.id.neq(id) } }

        query.andWhere { CustomerTable.completeName.eq(completeName) }
             .andWhere { CustomerTable.nickName.eq(nickName)}

        return query.count()
    }

    private fun toCustomerProjection(row: ResultRow) = CustomerTable.rowToCustomerProjection(row)

}