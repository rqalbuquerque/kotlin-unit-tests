package com.example

interface StorageConnector {
    fun save(content: String, path: String)
}