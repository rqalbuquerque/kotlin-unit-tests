package com.example.connector

import com.example.StorageConnector

class StorageFakeConnector: StorageConnector {
    override fun save(content: String, path: String) {
        println("$path")
        println()
        println(content)
    }
}