package com.miempresa.jsonplaceapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.miempresa.jsonplaceapp.domain.model.Post
import com.miempresa.jsonplaceapp.domain.model.User
import com.miempresa.jsonplaceapp.domain.repository.PostRepository
import com.miempresa.jsonplaceapp.domain.util.Resource
import com.miempresa.jsonplaceapp.util.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FilterState(
    val userId: Int?     = null,
    val titleQuery: String = ""
)

data class MainUiState(
    val users: List<User>         = emptyList(),
    val isLoadingUsers: Boolean   = false,
    val errorMessage: String?     = null,
    val isRefreshing: Boolean     = false
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PostRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    // ── Conectividad ────────────────────────────────────────────────────────
    val isOnline: StateFlow<Boolean> = networkMonitor.isOnline
        .stateIn(
            scope         = viewModelScope,
            started       = SharingStarted.WhileSubscribed(5_000),
            initialValue  = true
        )

    // ── Filtros ─────────────────────────────────────────────────────────────
    private val _filterState = MutableStateFlow(FilterState())
    val filterState: StateFlow<FilterState> = _filterState.asStateFlow()

    // ── Estado general UI ───────────────────────────────────────────────────
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    // ── Lista paginada ──────────────────────────────────────────────────────
    // flatMapLatest re-crea el Pager cada vez que cambia el filtro
    val pagedPosts: StateFlow<PagingData<Post>> = _filterState
        .flatMapLatest { filter ->
            repository.getPagedPosts(
                userId     = filter.userId,
                titleQuery = filter.titleQuery.ifBlank { null }
            )
        }
        .cachedIn(viewModelScope)           // sobrevive rotaciones
        .stateIn(
            scope        = viewModelScope,
            started      = SharingStarted.WhileSubscribed(5_000),
            initialValue = PagingData.empty()
        )

    // ── Init ─────────────────────────────────────────────────────────────────
    init {
        loadUsers()
    }

    // ── Filtros ──────────────────────────────────────────────────────────────
    fun filterByUserId(userId: Int?) {
        _filterState.update { it.copy(userId = userId) }
    }

    fun filterByTitle(query: String) {
        _filterState.update { it.copy(titleQuery = query) }
    }

    fun clearFilters() {
        _filterState.value = FilterState()
    }

    // ── Usuarios (para el selector de filtro) ────────────────────────────────
    private fun loadUsers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingUsers = true) }
            when (val result = repository.getUsers()) {
                is Resource.Success ->
                    _uiState.update { it.copy(users = result.data, isLoadingUsers = false) }
                is Resource.Error ->
                    _uiState.update { it.copy(errorMessage = result.message, isLoadingUsers = false) }
                Resource.Loading -> Unit
            }
        }
    }

    // ── Refresco manual (pull-to-refresh) ────────────────────────────────────
    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            repository.refreshPosts()
            _uiState.update { it.copy(isRefreshing = false) }
            loadUsers()
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}