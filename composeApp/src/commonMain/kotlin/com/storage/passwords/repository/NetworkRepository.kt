package com.storage.passwords.repository

import com.storage.passwords.models.PasswordItem
import com.storage.passwords.models.PasswordsResult
import com.storage.passwords.models.mapToEntity
import com.storage.passwords.models.mapToRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.koin.core.component.getScopeId
import kotlin.coroutines.cancellation.CancellationException

class NetworkRepository(
    private val networkModule: HttpClient,
    private val configRepository: ConfigRepository
) {

    suspend fun getData(pageNumber: Int): Result<List<PasswordsResult>> {
        return try {
            val passwords: Array<PasswordsResult> =
                networkModule.get("${configRepository.getBaseUrl()}/passwords").body()
            Result.success(passwords.asList())
        } catch (e: Exception) {
            if (e is CancellationException) {
                e.printStackTrace()
                Result.success(emptyList<PasswordsResult>())
            } else
                Result.failure(exception = e)
        }
    }

    suspend fun savePassword(passwordItem: PasswordItem): Result<String> {
        return try {
            val result : String =
                networkModule.post("${configRepository.getBaseUrl()}/submit-password"){
                    contentType(ContentType.Application.Json)
                    setBody(passwordItem.mapToRequest())
                }.body()
            Result.success(result)

        } catch (e: Exception) {
            if (e is CancellationException) {
                e.printStackTrace()
                Result.success("Ok")
            } else
                Result.failure(exception = e)
        }
    }
}
