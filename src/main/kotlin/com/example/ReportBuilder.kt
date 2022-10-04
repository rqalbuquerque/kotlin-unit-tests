package com.example

import com.example.model.Customer
import com.example.model.CustomerCredit

object ReportBuilder {
    fun buildAvailableCreditReport(customersCredit: List<CustomerCredit>): String {
        return customersCredit
            .joinToString("\n") {
                """{"customerId":"${it.customerId}","availableAmountE2":${it.availableAmountE2}}"""
            }
    }

    fun buildTotalAmountReport(customers: List<Customer>): String {
        return customers
            .joinToString("\n") {
                """{"customerId":"${it.id}","totalAmountE2":${it.totalAmountE2}}"""
            }
    }
}