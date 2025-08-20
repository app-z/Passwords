package com.storage.passwords.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

// Not implemented yet
object DispatchersRepository {
    fun io() = Dispatchers.IO
    fun main() = Dispatchers.Main
}
