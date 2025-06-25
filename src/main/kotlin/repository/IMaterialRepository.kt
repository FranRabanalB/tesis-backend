package com.example.repository

import dtos.MaterialDTO

interface IMaterialRepository {
    suspend fun getAll(): List<MaterialDTO>
    suspend fun create(material: MaterialDTO): MaterialDTO
    suspend fun update(id: Long, material: MaterialDTO): Boolean
    suspend fun delete(id: Long): Boolean
    // aquí podrías añadir update(), delete(), etc.
}
