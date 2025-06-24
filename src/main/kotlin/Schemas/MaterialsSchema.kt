// com.example.schema.Materials.kt
package com.example

import org.jetbrains.exposed.dao.id.LongIdTable

object Materials : LongIdTable("materials") {
    val nombre      = varchar("nombre", 100)
    val descripcion = varchar("descripcion", 255).nullable()
    val cantidad    = integer("cantidad").default(0)
    val valor       = double("valor")
    val unidad      = varchar("unidad", 20)
    val stockMinimo = integer("stock_minimo").default(0)
}
