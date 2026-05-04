package com.miempresa.jsonplaceapp.domain.repository

import androidx.paging.PagingData
import com.miempresa.jsonplaceapp.domain.model.Comment
import com.miempresa.jsonplaceapp.domain.model.Post
import com.miempresa.jsonplaceapp.domain.model.User
import com.miempresa.jsonplaceapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface PostRepository {

    // Lista paginada con filtros opcionales
    fun getPagedPosts(
        userId: Int?,
        titleQuery: String?
    ): Flow<PagingData<Post>>

    // Detalle de un post
    suspend fun getPostById(postId: Int): Resource<Post>

    // Comentarios de un post
    suspend fun getCommentsByPost(postId: Int): Resource<List<Comment>>

    // Lista de usuarios para el selector de filtro
    suspend fun getUsers(): Resource<List<User>>

    // Forzar sincronización con la API
    suspend fun refreshPosts(): Resource<Unit>
}