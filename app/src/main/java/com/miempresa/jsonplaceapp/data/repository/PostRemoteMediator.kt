package com.miempresa.jsonplaceapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.miempresa.jsonplaceapp.data.local.dao.PostDao
import com.miempresa.jsonplaceapp.data.local.entity.PostEntity
import com.miempresa.jsonplaceapp.data.mapper.toEntity
import com.miempresa.jsonplaceapp.data.remote.ApiService
import retrofit2.HttpException
import java.io.IOException

private const val PAGE_SIZE = 20

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator(
    private val apiService: ApiService,
    private val postDao: PostDao,
    private val userId: Int?        // null = sin filtro
) : RemoteMediator<Int, PostEntity>() {

    override suspend fun initialize(): InitializeAction {
        // Si ya hay datos locales, no forzar refresco al arrancar
        val count = postDao.getPostCount()
        return if (count > 0) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> 0

            LoadType.PREPEND ->
                // No necesitamos cargar hacia arriba
                return MediatorResult.Success(endOfPaginationReached = true)

            LoadType.APPEND -> {
                // Calculamos el offset según cuántos items hay en DB
                val lastItem = state.lastItemOrNull()
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
                // Offset = posición del último item + 1
                state.pages.sumOf { it.data.size }
            }
        }

        return try {
            val response = apiService.getPosts(
                start  = page,
                limit  = state.config.pageSize,
                userId = userId
            )

            val endOfPagination = response.size < state.config.pageSize

            if (loadType == LoadType.REFRESH) {
                // En refresh limpiamos solo si no hay filtro para no borrar cachés parciales
                if (userId == null) postDao.clearAll()
            }

            postDao.insertAll(response.map { it.toEntity() })

            MediatorResult.Success(endOfPaginationReached = endOfPagination)

        } catch (e: IOException) {
            // Sin conexión → Paging usará los datos locales de Room
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}