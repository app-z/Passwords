package com.storage.passwords

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform