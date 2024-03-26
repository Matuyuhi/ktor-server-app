package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*

fun Application.configureDatabases() {
    val jdbcDatabaseUrl = System.getenv("JDBC_DATABASE_URL")
        ?: "jdbc:mysql://localhost:3306/mydb"
    val username = System.getenv("JDBC_DATABASE_USERNAME") ?: "user"
    val password = System.getenv("JDBC_DATABASE_PASSWORD") ?: "password"
    val database = Database.connect(
        url = jdbcDatabaseUrl,
        driver = "com.mysql.cj.jdbc.Driver",
        user = username,
        password = password
    )
    val userService = UserService(database)
    routing {
        route("/users") {
            // Create user
            post {
                // curl -X POST -H "Content-Type: application/json" -d '{"name": "John Doe", "age": 22, "a":ssa}' http://localhost:8080/users
                val user = call.receive<ExposedUser>()
                val id = userService.create(user)
                call.respond(HttpStatusCode.Created, id)
            }

            get {
                val users = userService.readAll()
                call.respond(HttpStatusCode.OK, users)
            }
        }

        // Read user
        get("/users/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = userService.read(id)
            if (user != null) {
                call.respond(HttpStatusCode.OK, user)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        // Update user
        put("/users/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = call.receive<ExposedUser>()
            userService.update(id, user)
            call.respond(HttpStatusCode.OK)
        }

        // Delete user
        delete("/users/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            userService.delete(id)
            call.respond(HttpStatusCode.OK)
        }
    }
}
