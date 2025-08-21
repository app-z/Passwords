package com.storage.passwords.repository

import com.storage.passwords.BuildKonfig

class ConfigRepository {

    val isDebugBuild = BuildKonfig.Is_Debug_Server

    fun getBaseUrl(): String {
        return if (isDebugBuild)
            BASE_URL_DEBUG
        else
            BASE_URL_RELEASE
    }

    companion object Companion {
        const val BASE_URL_RELEASE = "https://0.0.0.0:8080"
        const val BASE_URL_DEBUG = "http://192.168.1.40:8080"
    }

}