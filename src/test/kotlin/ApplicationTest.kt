package com.example

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import io.ktor.server.config.*
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun testMaterials() = testApplication {
        environment {
            config = MapApplicationConfig(
                "ktor.deployment.module" to "",
                "db.url" to "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
                "db.user" to "sa",
                "db.password" to ""
            )
        }
        application {
            module()
        }
        client.get("/materials").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

}
