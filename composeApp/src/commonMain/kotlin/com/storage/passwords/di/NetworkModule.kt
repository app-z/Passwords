package com.storage.passwords.di

import com.storage.passwords.BuildKonfig
import com.storage.passwords.repository.ConfigRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single {
        HttpClient {
            defaultRequest {
                url(ConfigRepository().getBaseUrl())
                contentType(ContentType.Application.Json)
            }
            install(HttpTimeout) {
                socketTimeoutMillis = 60_000
                requestTimeoutMillis = 60_000
            }
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = false
                    ignoreUnknownKeys = true
                    explicitNulls = false
                })
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        message
                    }
                }
            }
        }
    }
}