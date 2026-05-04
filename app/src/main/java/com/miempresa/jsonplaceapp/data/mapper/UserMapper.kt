package com.miempresa.jsonplaceapp.data.mapper

import com.miempresa.jsonplaceapp.data.local.entity.UserEntity
import com.miempresa.jsonplaceapp.data.remote.dto.UserDto
import com.miempresa.jsonplaceapp.domain.model.User

fun UserDto.toEntity(): UserEntity = UserEntity(
    id          = id,
    name        = name,
    username    = username,
    email       = email,
    phone       = phone,
    website     = website,
    companyName = company.name,
    city        = address.city
)

fun UserEntity.toDomain(): User = User(
    id          = id,
    name        = name,
    username    = username,
    email       = email,
    phone       = phone,
    website     = website,
    companyName = companyName,
    city        = city
)

fun UserDto.toDomain(): User = User(
    id          = id,
    name        = name,
    username    = username,
    email       = email,
    phone       = phone,
    website     = website,
    companyName = company.name,
    city        = address.city
)class UserMapper {
}