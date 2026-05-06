package com.miempresa.jsonplaceapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.miempresa.jsonplaceapp.data.local.dao.PostDao
import com.miempresa.jsonplaceapp.data.local.entity.PostEntity
import com.miempresa.jsonplaceapp.data.mapper.toEntity
import com.miempresa.jsonplaceapp.data.remote.ApiService
import com.miempresa.jsonplaceapp.util.NetworkMonitor
import kotlinx.coroutines.flow.first // Importante para leer el estado de red
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator(
    private val apiService: ApiService,
    private val postDao: PostDao,
    private val networkMonitor: NetworkMonitor,
    private val userId: Int?
) : RemoteMediator<Int, PostEntity>() {

    override suspend fun initialize(): InitializeAction {
        val count = postDao.getPostCount()

        // Obtenemos el último valor emitido por el monitor de red
        val isOnline = networkMonitor.isOnline.first()

        return if (isOnline) {
            // Si hay internet, refrescamos para asegurar consistencia
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else if (count > 0) {
            // Si no hay internet pero hay datos, mostramos lo que tenemos
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            // Si no hay nada de nada, intentamos cargar (aunque falle, es el flujo inicial)
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                // Offset basado en la cantidad de items actuales en la base de datos
                state.pages.sumOf { it.data.size }
            }
        }

        return try {
            val response = apiService.getPosts(
                start = page,
                limit = state.config.pageSize,
                userId = userId
            )

            val endOfPagination = response.size < state.config.pageSize

            if (loadType == LoadType.REFRESH && userId == null) {
                // Solo limpiamos la base de datos si es una carga general (sin filtros)
                postDao.clearAll()
            }

            postDao.insertAll(response.map { it.toEntity() })

            MediatorResult.Success(endOfPaginationReached = endOfPagination)

        } catch (e: IOException) {
            // Error de red o falta de conexión
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            // Error del servidor (404, 500, etc.)
            MediatorResult.Error(e)
        } catch (e: Exception) {
            // Cualquier otro error inesperado
            MediatorResult.Error(e)
        }
    }
}