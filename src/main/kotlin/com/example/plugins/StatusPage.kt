package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*


internal fun Application.statusPage() {
    install(StatusPages) {
        status(HttpStatusCode.NotFound) { applicationCall: ApplicationCall, httpStatusCode: HttpStatusCode ->
            applicationCall.respondText("NotFound")
        }
        exception<Throwable> { call, cause ->
            call.respondText("Error: ${cause.localizedMessage}", status = HttpStatusCode.InternalServerError)
        }
        // statusFile(HttpStatusCode.Unauthorized, HttpStatusCode.PaymentRequired, filePattern = "error#.html")
    }
}