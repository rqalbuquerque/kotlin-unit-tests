package com.example

import com.example.model.Contract
import com.example.model.Customer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreditCalculatorTest {
    @Test
    fun `should return the total amount when there are no contracts`() {
        val contracts = emptyList<Contract>()
        val expected = defaultCustomer.totalAmountE2

        val result = CreditCalculator.calcAvailableCredit(defaultCustomer, contracts)

        assertEquals(expected, result)
    }

    @Test
    fun `should return the total amount when there are no contracts for customer`() {
        val contracts = listOf(Contract("99", "99", 10000, false))
        val expected = defaultCustomer.totalAmountE2

        val result = CreditCalculator.calcAvailableCredit(defaultCustomer, contracts)

        assertEquals(expected, result)
    }

    @Test
    fun `should return the total amount when all contracts are paid`() {
        val contracts = listOf(
            Contract("11", "00", 10000, true),
            Contract("12", "00", 12000, true),
            Contract("13", "00", 4000, true))
        val expected = defaultCustomer.totalAmountE2

        val result = CreditCalculator.calcAvailableCredit(defaultCustomer, contracts)

        assertEquals(expected, result)
    }

    @Test
    fun `should return the available amount when there are contracts not paid`() {
        val contracts = listOf(
            Contract("11", "00", 1000, false),
            Contract("12", "00", 1200, true),
            Contract("13", "00", 4000, false))
        val expected = 5000L

        val result = CreditCalculator.calcAvailableCredit(defaultCustomer, contracts)

        assertEquals(expected, result)
    }

    companion object {
        private val defaultCustomer = Customer("00", "customer0", 10000, true)
    }
}