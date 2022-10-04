package com.example

import com.example.model.Contract

interface ContractDal {
    fun list(customerId: String): List<Contract>
    fun save(contract: Contract)
}