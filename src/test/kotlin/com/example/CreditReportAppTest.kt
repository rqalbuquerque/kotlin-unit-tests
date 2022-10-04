package com.example

import com.example.model.Contract
import com.example.model.Customer
import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.LocalDate

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreditReportAppTest {
    private val customerDalMock: CustomerDal = mockk()
    private val contractDalMock: ContractDal = mockk()
    private val storageConnectorMock: StorageConnector = mockk()

    private val reportApp: CreditReportApp = CreditReportApp(
        customerDalMock,
        contractDalMock,
        storageConnectorMock
    )

    @BeforeEach
    fun setup() {
        every { customerDalMock.list() } returns customers
    }

    @AfterEach
    fun teardown() {
        confirmVerified(
            customerDalMock,
            contractDalMock,
            storageConnectorMock)
        clearAllMocks()
    }

    @Test
    fun `should not generate a report when there are no customers`() {
        every { customerDalMock.list() } returns emptyList()

        reportApp.run(defaultDate)

        verify {
            customerDalMock.list()
            contractDalMock.list(any()) wasNot called
        }
    }

    @Test
    fun `should generate a report and save it to output storage`() {
        val reportSlot = slot<String>()
        val outputPathSlot = slot<String>()

        val expectedReport = """
            {"customerId":"00","availableAmountE2":5000}
            {"customerId":"01","availableAmountE2":16000}
        """.trimIndent()
        val expectedOutputPath = "reports/available-credit-2022-01-01.json"

        every { contractDalMock.list(customers[0].id) } returns contracts[customers[0].id]!!
        every { contractDalMock.list(customers[1].id) } returns contracts[customers[1].id]!!
        every { storageConnectorMock.save(capture(reportSlot), capture(outputPathSlot)) } just Runs

        reportApp.run(defaultDate)

        assertEquals(expectedReport, reportSlot.captured)
        assertEquals(expectedOutputPath, outputPathSlot.captured)

        verifySequence {
            customerDalMock.list()
            contractDalMock.list(customers[0].id)
            contractDalMock.list(customers[1].id)
            storageConnectorMock.save(reportSlot.captured, outputPathSlot.captured)
        }
    }

    @Test
    fun `should throw an error when customer DAL gets an error`() {
        val exception = Exception("Customer DAL Error")

        every { customerDalMock.list() } throws exception

        assertThrows(exception.javaClass) {
            reportApp.run(defaultDate)
        }

        verify { customerDalMock.list() }
    }

    @Test
    fun `should throw an error when contract DAL gets an error`() {
        val exception = Exception("Contract DAL Error")

        every { contractDalMock.list(any()) } throws exception

        assertThrows(exception.javaClass) {
            reportApp.run(defaultDate)
        }

        verifySequence {
            customerDalMock.list()
            contractDalMock.list(customers[0].id)
        }
    }

    @Test
    fun `should throw an error when storage connector gets an error`() {
        val exception = Exception("Storage Connector Error")

        every { contractDalMock.list(any()) } returns emptyList()
        every { storageConnectorMock.save(any(), any()) } throws exception

        assertThrows(exception.javaClass) {
            reportApp.run(defaultDate)
        }

        verifySequence {
            customerDalMock.list()
            contractDalMock.list(customers[0].id)
            contractDalMock.list(customers[1].id)
            storageConnectorMock.save(any(), any())
        }
    }

    companion object {
        private val defaultDate: LocalDate = LocalDate.of(2022,1,1)
        private val customers = listOf(
            Customer("00", "customer0", 10000, true),
            Customer("01", "customer1", 20000, true)
        )
        private val contracts = mapOf(
            "00" to listOf(
                Contract("01", "00", 1000, false),
                Contract("02", "00", 1200, true),
                Contract("03", "00", 4000, false)
            ),
            "01" to listOf(
                Contract("11", "01", 1000, true),
                Contract("13", "01", 4000, false)
            )
        )

        // helper functions
    }
}