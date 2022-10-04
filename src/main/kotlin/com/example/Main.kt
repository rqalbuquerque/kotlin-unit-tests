package com.example

import com.example.dal.CustomerFakeDal
import com.example.dal.ContractFakeDal
import com.example.connector.StorageFakeConnector

import java.time.LocalDate

fun main(args: Array<String>) {
    // parse args

    // create dependencies
    val customerDal = CustomerFakeDal()
    val contractDal = ContractFakeDal()
    val storageConnector = StorageFakeConnector()

    // create application
    val creditReporter = CreditReportApp(
        customerDal,
        contractDal,
        storageConnector)

    creditReporter.run(LocalDate.now())
}