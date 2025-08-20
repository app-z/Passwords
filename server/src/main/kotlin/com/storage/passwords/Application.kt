package com.storage.passwords

import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    routing {
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }

        get("/passwords") {
            call.response.headers.append(
                HttpHeaders.ContentType,
                ContentType.Application.Json.toString()
            )
            call.respondText(
                """
                    [ 
                    { "id":"1", "name":"password1", "password":"ADAD%ADAD", "note":"I note it"},
                    { "id":"2", "name":"password2", "password":"1ADAD%ADAD", "note":"I note it eee"},
                    { "id":"3", "name":"password3", "password":"2ADAD%ADAD", "note":"I note it fff"}
                    ]
                    """.trimMargin()
            )
        }

        get("/submit") {
            val receivedData = call.receive<String>() // Assuming plain text data

            // Process the received data
            println("Received POST data: $receivedData")

            // Send a response back to the client
            call.respondText("Data received successfully!")
        }

        post("/submit-password") {
            // Receive the data from the request body
            val receivedData = call.receive<String>() // Assuming plain text data

            // Process the received data
            println("Received POST data: $receivedData")

            // Send a response back to the client
            call.respondText("Data received successfully!")
        }

    }
}