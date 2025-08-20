package com.storage.passwords.repository

import com.storage.passwords.di.BASE_URL
import com.storage.passwords.models.PasswordsResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlin.coroutines.cancellation.CancellationException

class NetworkRepository(
    private val networkModule: HttpClient
) {

    suspend fun getData(pageNumber: Int): Result<List<PasswordsResult>> {
        return try {
            val passwords: Array<PasswordsResult> =
                networkModule.get("$BASE_URL/passwords").body()
            Result.success(passwords.asList())
        } catch (e: Exception) {
            if (e is CancellationException) {
                e.printStackTrace()
                Result.success(emptyList<PasswordsResult>())
            } else
                Result.failure(exception = e)
        }
    }
}
