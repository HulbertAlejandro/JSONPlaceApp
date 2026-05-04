package com.miempresa.jsonplaceapp.data.mapper

import com.miempresa.jsonplaceapp.data.local.entity.PhotoEntity
import com.miempresa.jsonplaceapp.data.remote.dto.PhotoDto
import com.miempresa.jsonplaceapp.domain.model.Photo

fun PhotoDto.toEntity(): PhotoEntity = PhotoEntity(
    id           = id,
    albumId      = albumId,
    title        = title,
    url          = url,
    thumbnailUrl = thumbnailUrl
)

fun PhotoEntity.toDomain(): Photo = Photo(
    id           = id,
    albumId      = albumId,
    title        = title,
    url          = url,
    thumbnailUrl = thumbnailUrl
)

fun PhotoDto.toDomain(): Photo = Photo(
    id           = id,
    albumId      = albumId,
    title        = title,
    url          = url,
    thumbnailUrl = thumbnailUrl
)