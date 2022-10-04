package com.example

import com.example.model.Contract
import com.example.model.Customer

object CreditCalculator {
    fun calcAvailableCredit(customer: Customer, contracts: List<Contract>): Long {
        val encumberedAmountE2 = contracts
            .filter { customer.id == it.customerId && !it.paid }
            .sumOf { it.amountE2 }

        return customer.totalAmountE2 - encumberedAmountE2
    }
}