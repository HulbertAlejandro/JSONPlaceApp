package com.miempresa.jsonplaceapp.data.repository

import androidx.paging.*
import com.miempresa.jsonplaceapp.data.local.AppDatabase
import com.miempresa.jsonplaceapp.data.local.dao.CommentDao
import com.miempresa.jsonplaceapp.data.local.dao.PostDao
import com.miempresa.jsonplaceapp.data.local.dao.UserDao
import com.miempresa.jsonplaceapp.data.local.entity.PostEntity
import com.miempresa.jsonplaceapp.data.mapper.toDomain
import com.miempresa.jsonplaceapp.data.mapper.toEntity
import com.miempresa.jsonplaceapp.data.remote.ApiService
import com.miempresa.jsonplaceapp.domain.model.Comment
import com.miempresa.jsonplaceapp.domain.model.Post
import com.miempresa.jsonplaceapp.domain.model.User
import com.miempresa.jsonplaceapp.domain.repository.PostRepository
import com.miempresa.jsonplaceapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val PAGE_SIZE = 20

@OptIn(ExperimentalPagingApi::class)
class PostRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val postDao: PostDao,
    private val userDao: UserDao,
    private val commentDao: CommentDao,
    private val database: AppDatabase
) : PostRepository {

    override fun getPagedPosts(
        userId: Int?,
        titleQuery: String?
    ): Flow<PagingData<Post>> {

        // Selecciona la fuente local según los filtros activos
        val pagingSourceFactory: () -> PagingSource<Int, *> = {
            when {
                userId != null && !titleQuery.isNullOrBlank() -> { postDao.getPostsByUserIdAndTitlePaged(userId, titleQuery) }
                userId != null -> { postDao.getPostsByUserIdPaged(userId) }
                !titleQuery.isNullOrBlank() -> { postDao.getPostsByTitlePaged(titleQuery) }
                else -> { postDao.getPostsPaged() }
            }
        }

        return Pager(
            config = PagingConfig(
                pageSize         = PAGE_SIZE,
                prefetchDistance = 5,
                enablePlaceholders = false
            ),
            remoteMediator = PostRemoteMediator(
                apiService = apiService,
                postDao    = postDao,
                userId     = userId
            ),
            pagingSourceFactory = @Suppress("UNCHECKED_CAST")
            (pagingSourceFactory as () -> PagingSource<Int, PostEntity> as () -> PagingSource<Int, PostEntity>)
        ).flow.map { pagingData ->
            pagingData.map { entity -> entity.toDomain() }
        }
    }

    override suspend fun getPostById(postId: Int): Resource<Post> {
        return try {
            // Primero intentamos la API para tener datos frescos
            val dto = apiService.getPostById(postId)
            postDao.insert(dto.toEntity())
            Resource.Success(dto.toDomain())
        } catch (e: IOException) {
            // Sin internet → buscamos en Room
            val local = postDao.getPostById(postId)
            if (local != null) Resource.Success(local.toDomain())
            else Resource.Error("Sin conexión y no hay datos locales para este post.")
        } catch (e: HttpException) {
            Resource.Error("Error del servidor: ${e.code()}")
        }
    }

    override suspend fun getCommentsByPost(postId: Int): Resource<List<Comment>> {
        return try {
            val dtos = apiService.getCommentsByPost(postId)
            commentDao.deleteCommentsByPostId(postId)
            commentDao.insertAll(dtos.map { it.toEntity() })
            Resource.Success(dtos.map { it.toDomain() })
        } catch (e: IOException) {
            val local = commentDao.getCommentsByPostId(postId)
            if (local.isNotEmpty()) Resource.Success(local.map { it.toDomain() })
            else Resource.Error("Sin conexión y no hay comentarios almacenados.")
        } catch (e: HttpException) {
            Resource.Error("Error del servidor: ${e.code()}")
        }
    }

    override suspend fun getUsers(): Resource<List<User>> {
        return try {
            val dtos = apiService.getUsers()
            userDao.insertAll(dtos.map { it.toEntity() })
            Resource.Success(dtos.map { it.toDomain() })
        } catch (e: IOException) {
            val local = userDao.getAllUsers()
            if (local.isNotEmpty()) Resource.Success(local.map { it.toDomain() })
            else Resource.Error("Sin conexión y no hay usuarios almacenados.")
        } catch (e: HttpException) {
            Resource.Error("Error del servidor: ${e.code()}")
        }
    }

    override suspend fun refreshPosts(): Resource<Unit> {
        return try {
            val dtos = apiService.getPosts(start = 0, limit = PAGE_SIZE)
            postDao.clearAll()
            postDao.insertAll(dtos.map { it.toEntity() })
            Resource.Success(Unit)
        } catch (e: IOException) {
            Resource.Error("Sin conexión a internet.")
        } catch (e: HttpException) {
            Resource.Error("Error del servidor: ${e.code()}")
        }
    }
}