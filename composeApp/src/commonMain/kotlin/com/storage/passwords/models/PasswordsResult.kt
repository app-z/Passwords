package com.storage.passwords.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PasswordsResult(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("password") val password: String,
    @SerialName("note") val note: String,
    @SerialName("date_utc") var dateUtc: String? = null
)