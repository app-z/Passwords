package com.storage.passwords.repository

import com.storage.passwords.BuildKonfig

class ConfigRepository {

    private val isDebugBuild = BuildKonfig.Is_Debug_Server

    fun getBaseUrl(): String {
        return if (isDebugBuild)
            BASE_URL_DEBUG
        else
            BASE_URL_RELEASE
    }

    companion object Companion {
        private const val BASE_URL_RELEASE = "https://0.0.0.0:8080"
        private const val BASE_URL_DEBUG = "http://192.168.1.215:8080"
    }

}