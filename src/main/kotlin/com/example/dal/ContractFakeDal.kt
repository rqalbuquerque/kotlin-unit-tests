package com.example.dal

import com.example.model.Contract
import com.example.ContractDal

class ContractFakeDal: ContractDal {
    override fun list(customerId: String): List<Contract> {
        return when(customerId) {
            "01" -> listOf(
                Contract("11", "01", 10000, false),
                Contract("12", "01", 12000, true),
                Contract("13", "01", 4000, false),
            )
            "02" -> listOf(
                Contract("21", "02", 3000, false),
                Contract("22", "02", 1500, true),
                Contract("23", "02", 20000, false),
                Contract("23", "02", 45000, true),
            )
            else -> emptyList()
        }
    }

    override fun save(contract: Contract) {
        TODO("Not yet implemented")
    }
}