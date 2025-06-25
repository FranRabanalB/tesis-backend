package com.example.repository


import com.example.Materials
import dtos.MaterialDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class ExposedMaterialRepository : IMaterialRepository {
    override suspend fun getAll(): List<MaterialDTO> = transaction {
        Materials.selectAll().map {
            MaterialDTO(
                id          = it[Materials.id].value,
                nombre      = it[Materials.nombre],
                descripcion = it[Materials.descripcion],
                cantidad    = it[Materials.cantidad],
                valor       = it[Materials.valor],
                unidad      = it[Materials.unidad],
                stockMinimo = it[Materials.stockMinimo]
            )
        }
    }

    override suspend fun create(material: MaterialDTO): MaterialDTO {
        val newId = transaction {
            Materials.insertAndGetId {
                it[nombre]      = material.nombre
                it[descripcion] = material.descripcion
                it[cantidad]    = material.cantidad
                it[valor]       = material.valor
                it[unidad]      = material.unidad
                it[stockMinimo] = material.stockMinimo
            }
        }.value

        return material.copy(id = newId)
    }


    override suspend fun update(id: Long, material: MaterialDTO): Boolean = transaction {
        Materials.update({ Materials.id eq id }) {
            it[nombre]      = material.nombre
            it[descripcion] = material.descripcion
            it[cantidad]    = material.cantidad
            it[valor]       = material.valor
            it[unidad]      = material.unidad
            it[stockMinimo] = material.stockMinimo
        } > 0
    }


    override suspend fun delete(id: Long): Boolean = transaction {
        Materials.deleteWhere { Materials.id eq id } > 0
    }
}
