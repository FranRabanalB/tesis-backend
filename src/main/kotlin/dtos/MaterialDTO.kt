package dtos

import kotlinx.serialization.Serializable

@Serializable
data class MaterialDTO(
    val id: Long? = null,
    val nombre: String,
    val descripcion: String?,
    val cantidad: Int,
    val valor: Double,
    val unidad: String,
    val stockMinimo: Int
)