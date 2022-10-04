package com.example

import com.example.model.Customer

interface CustomerDal {
    fun get(id: String): Customer?
    fun list(active: Boolean = true): List<Customer>
    fun save(customer: Customer)
}