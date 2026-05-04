package com.miempresa.jsonplaceapp.data.remote

import com.miempresa.jsonplaceapp.data.remote.dto.CommentDto
import com.miempresa.jsonplaceapp.data.remote.dto.PhotoDto
import com.miempresa.jsonplaceapp.data.remote.dto.PostDto
import com.miempresa.jsonplaceapp.data.remote.dto.UserDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // Endpoint 1: Lista paginada de posts (filtro por userId)
    @GET("posts")
    suspend fun getPosts(
        @Query("_start") start: Int,
        @Query("_limit") limit: Int,
        @Query("userId") userId: Int? = null
    ): List<PostDto>

    // Endpoint 2: Detalle de un post específico
    @GET("posts/{id}")
    suspend fun getPostById(
        @Path("id") postId: Int
    ): PostDto

    // Endpoint 3: Comentarios de un post específico
    @GET("posts/{id}/comments")
    suspend fun getCommentsByPost(
        @Path("id") postId: Int
    ): List<CommentDto>

    // Endpoint 4a: Lista de usuarios
    @GET("users")
    suspend fun getUsers(): List<UserDto>

    // Endpoint 4b: Fotos de un álbum (búsqueda enriquecida)
    @GET("photos")
    suspend fun getPhotos(
        @Query("albumId") albumId: Int? = null,
        @Query("_start") start: Int = 0,
        @Query("_limit") limit: Int = 10
    ): List<PhotoDto>
}