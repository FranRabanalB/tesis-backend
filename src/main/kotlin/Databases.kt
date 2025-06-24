package com.example

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.transactions.transaction
import com.example.Materials     // añade tus otras tablas si las tienes
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils


fun Application.configureDatabases() {

    val cfg = environment.config
    val jdbcUrl = cfg.propertyOrNull("postgres.url")?.getString()

    Database.connect(
        "jdbc:postgresql://pgsqltrans.face.ubiobio.cl:5432/frabanal_bd",
        user = "frabanal",
        password = "francisca2025"
    )

    transaction {
        SchemaUtils.create(Materials /*, Products, ProductMaterials */)
    }

}

/*
postgres:
    url: ${JDBC_URL:'jdbc:postgresql://pgsqltrans.face.ubiobio.cl:5432/frabanal_bd'}
    user: ${DB_USER:frabanal}
    password: ${DB_PASSWORD:francisca2025}*/