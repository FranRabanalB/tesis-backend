package com.example

import com.example.repository.ExposedMaterialRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import dtos.MaterialDTO
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import com.example.repository.IMaterialRepository


fun Route.materialRoutes(repo: IMaterialRepository) {
    route("/materials") {
        get {
            call.respond(repo.getAll())
        }
        post {
            val dto = call.receive<MaterialDTO>()
            call.respond(HttpStatusCode.Created, repo.create(dto))
        }
        put("{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
                ?: return@put call.respond(HttpStatusCode.BadRequest, "ID inválido")
            val dto = call.receive<MaterialDTO>()
            if (repo.update(id, dto)) call.respond(HttpStatusCode.OK)
            else call.respond(HttpStatusCode.NotFound)
        }
        delete("{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
                ?: return@delete call.respond(HttpStatusCode.BadRequest, "ID inválido")
            if (repo.delete(id)) call.respond(HttpStatusCode.OK)
            else call.respond(HttpStatusCode.NotFound)
        }
    }
}

fun Application.configureRouting() {

    // aquí creas tu repositorio concreto
    val repo: IMaterialRepository = ExposedMaterialRepository()

    routing {
        // monta las rutas pasándole el repo
        get("/") { call.respondText("Inventory API OK") }
        materialRoutes(repo)
    }
}

