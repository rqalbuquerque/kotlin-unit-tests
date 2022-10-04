package com.example.dal

import com.example.model.Customer
import com.example.CustomerDal

class CustomerFakeDal: CustomerDal {
    private val customers = listOf(
        Customer("00", "customer0", 100000, true),
        Customer("01", "customer1", 80000, true),
        Customer("02", "customer2", 450000, true)
    )

    override fun get(id: String): Customer? {
        return when(id) {
            "00" -> customers[0]
            "01" -> customers[1]
            "02" -> customers[2]
            else -> null
        }
    }

    override fun list(active: Boolean): List<Customer> = customers

    override fun save(customer: Customer) {
        TODO("Not yet implemented")
    }
}