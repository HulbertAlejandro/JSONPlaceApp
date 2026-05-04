package com.miempresa.jsonplaceapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id")       val id: Int,
    @SerializedName("name")     val name: String,
    @SerializedName("username") val username: String,
    @SerializedName("email")    val email: String,
    @SerializedName("phone")    val phone: String,
    @SerializedName("website")  val website: String,
    @SerializedName("company")  val company: CompanyDto,
    @SerializedName("address")  val address: AddressDto
)

data class CompanyDto(
    @SerializedName("name") val name: String
)

data class AddressDto(
    @SerializedName("city") val city: String
)