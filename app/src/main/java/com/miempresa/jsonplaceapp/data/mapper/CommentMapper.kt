package com.miempresa.jsonplaceapp.data.mapper

import com.miempresa.jsonplaceapp.data.local.entity.CommentEntity
import com.miempresa.jsonplaceapp.data.remote.dto.CommentDto
import com.miempresa.jsonplaceapp.domain.model.Comment

fun CommentDto.toEntity(): CommentEntity = CommentEntity(
    id     = id,
    postId = postId,
    name   = name,
    email  = email,
    body   = body
)

fun CommentEntity.toDomain(): Comment = Comment(
    id     = id,
    postId = postId,
    name   = name,
    email  = email,
    body   = body
)

fun CommentDto.toDomain(): Comment = Comment(
    id     = id,
    postId = postId,
    name   = name,
    email  = email,
    body   = body
)