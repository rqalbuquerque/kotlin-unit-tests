package com.example

import com.example.model.Customer
import com.example.model.CustomerCredit
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestInstance
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReportBuilderTest {
    @Nested
    inner class BuildAvailableCreditReport {
        @Test
        fun `should generate an empty report when input list is empty`() {
            val customersCredit = emptyList<CustomerCredit>()
            val expected = ""

            val result = ReportBuilder.buildAvailableCreditReport(customersCredit)

            assertEquals(expected, result)
        }

        @Test
        fun `should generate a report with customer's available credit in json lines`() {
            val customersCredit = listOf(
                CustomerCredit("01", 15000, 7500),
                CustomerCredit("02", 24000, 23500)
            )
            val expected = """
            |{"customerId":"01","availableAmountE2":7500}
            |{"customerId":"02","availableAmountE2":23500}"""
                .trimMargin()

            val result = ReportBuilder.buildAvailableCreditReport(customersCredit)

            assertEquals(expected, result)
        }
    }

    @Nested
    inner class BuildTotalAmountReport {
        @Test
        fun `should generate an empty report when input list is empty`() {
            val customersCredit = emptyList<Customer>()
            val expected = ""

            val result = ReportBuilder.buildTotalAmountReport(customersCredit)

            assertEquals(expected, result)
        }

        @Test
        fun `should generate a report with customer's total amount in json lines`() {
            val customers = listOf(
                Customer("01", "customer01", 15000, true),
                Customer("02", "customer02", 24000, false)
            )
            val expected = """
                |{"customerId":"01","totalAmountE2":15000}
                |{"customerId":"02","totalAmountE2":24000}"""
                .trimMargin()

            val result = ReportBuilder.buildTotalAmountReport(customers)

            assertEquals(expected, result)
        }
    }
}