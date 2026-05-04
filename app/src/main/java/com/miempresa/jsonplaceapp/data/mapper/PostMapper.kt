package com.miempresa.jsonplaceapp.data.mapper

import com.miempresa.jsonplaceapp.data.local.entity.PostEntity
import com.miempresa.jsonplaceapp.data.remote.dto.PostDto
import com.miempresa.jsonplaceapp.domain.model.Post

// Red → Base de datos local
fun PostDto.toEntity(): PostEntity = PostEntity(
    id     = id,
    userId = userId,
    title  = title,
    body   = body
)

// Base de datos local → Dominio (lo que la UI consume)
fun PostEntity.toDomain(): Post = Post(
    id     = id,
    userId = userId,
    title  = title,
    body   = body
)

// Atajo directo Red → Dominio (útil en el Repository)
fun PostDto.toDomain(): Post = Post(
    id     = id,
    userId = userId,
    title  = title,
    body   = body
)