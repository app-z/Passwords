package com.storage.passwords.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

object DispatchersRepository {
    val DispatchersIO = Dispatchers.IO
    val DispatchersMain = Dispatchers.Main
    val DispatchersUnconfined = Dispatchers.Unconfined
    val DispatchersDefault = Dispatchers.Default
}
