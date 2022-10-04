package com.example

import com.example.model.Customer
import com.example.model.Contract
import com.example.model.CustomerCredit
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CreditReportApp(
    private val customerDal: CustomerDal,
    private val contractDal: ContractDal,
    private val storageConnector: StorageConnector
) {
    companion object {
        private val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    }

    fun run(currentDate: LocalDate) {
        val outputPath = buildOutputPath(currentDate)

        val customerContracts = loadCustomerContracts()

        if(customerContracts.isNotEmpty()) {
            val customerCredit = buildCustomerCredit(customerContracts)
            val report = ReportBuilder.buildAvailableCreditReport(customerCredit)

            storageConnector.save(report, outputPath)
        }
    }

    private fun loadCustomerContracts(): List<Pair<Customer, List<Contract>>> {
        val customers = customerDal.list()

        return customers.map { Pair(it, contractDal.list(it.id)) }
    }

    private fun buildCustomerCredit(
        customerContracts: List<Pair<Customer, List<Contract>>>
    ): List<CustomerCredit> {
        return customerContracts.map {
            val (customer, contracts) = it
            val availableAmountE2 = CreditCalculator.calcAvailableCredit(customer, contracts)
            CustomerCredit(customer.id, customer.totalAmountE2, availableAmountE2)
        }
    }


    private fun buildOutputPath(currentDate: LocalDate): String {
        val date = currentDate.format(dateTimeFormatter)
        val prefix = "reports/available-credit-"

        return "$prefix$date.json"
    }
}