package com.example

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import dtos.MaterialDTO
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq


fun Application.configureRouting() = routing {

    /** Ruta raíz opcional */
    get("/") { call.respondText("Back-end activo 👍") }

    /** ----------------- CRUD de Materiales ----------------- */
    route("/materials") {

        /** Listar todos */
        get {
            val lista = transaction {
                Materials.selectAll().map {
                    MaterialDTO(
                        id           = it[Materials.id].value,
                        nombre       = it[Materials.nombre],
                        descripcion  = it[Materials.descripcion],
                        cantidad     = it[Materials.cantidad],
                        valor        = it[Materials.valor],
                        unidad       = it[Materials.unidad],
                        stockMinimo  = it[Materials.stockMinimo]
                    )
                }
            }
            call.respond(lista)
        }

        /** Crear nuevo material */
        post {
            val dto = call.receive<MaterialDTO>()
            val newId = transaction {
                Materials.insertAndGetId {
                    it[nombre]      = dto.nombre
                    it[descripcion] = dto.descripcion
                    it[cantidad]    = dto.cantidad
                    it[valor]       = dto.valor
                    it[unidad]      = dto.unidad
                    it[stockMinimo] = dto.stockMinimo
                }.value
            }
            call.respond(HttpStatusCode.Created, dto.copy(id = newId))
        }

        /** Actualizar material */
        put("{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
                ?: return@put call.respond(HttpStatusCode.BadRequest, "ID inválido")

            val dto = call.receive<MaterialDTO>()
            val filas = transaction {
                Materials.update({ Materials.id eq id }) {
                    it[nombre]      = dto.nombre
                    it[descripcion] = dto.descripcion
                    it[cantidad]    = dto.cantidad
                    it[valor]       = dto.valor
                    it[unidad]      = dto.unidad
                    it[stockMinimo] = dto.stockMinimo
                }
            }
            if (filas > 0) call.respond(HttpStatusCode.OK)
            else call.respond(HttpStatusCode.NotFound)
        }

        /** Eliminar material */
        delete("{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
                ?: return@delete call.respond(HttpStatusCode.BadRequest, "ID inválido")

            val filas = transaction { Materials.deleteWhere { Materials.id eq id } }
            if (filas > 0) call.respond(HttpStatusCode.OK)
            else call.respond(HttpStatusCode.NotFound)
        }
    }
}

