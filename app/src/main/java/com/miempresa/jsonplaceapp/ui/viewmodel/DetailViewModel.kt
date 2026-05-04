package com.miempresa.jsonplaceapp.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miempresa.jsonplaceapp.domain.model.Comment
import com.miempresa.jsonplaceapp.domain.model.Post
import com.miempresa.jsonplaceapp.domain.repository.PostRepository
import com.miempresa.jsonplaceapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailUiState(
    val post: Post?               = null,
    val comments: List<Comment>   = emptyList(),
    val isLoading: Boolean        = true,
    val errorMessage: String?     = null
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: PostRepository,
    savedStateHandle: SavedStateHandle          // Hilt inyecta el postId desde la navegación
) : ViewModel() {

    private val postId: Int = checkNotNull(savedStateHandle["postId"])

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        loadDetail()
    }

    private fun loadDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Cargar post y comentarios en paralelo
            val postResult    = repository.getPostById(postId)
            val commentResult = repository.getCommentsByPost(postId)

            val post = when (postResult) {
                is Resource.Success -> postResult.data
                is Resource.Error   -> { _uiState.update { it.copy(errorMessage = postResult.message) }; null }
                Resource.Loading    -> null
            }

            val comments = when (commentResult) {
                is Resource.Success -> commentResult.data
                is Resource.Error   -> emptyList()
                Resource.Loading    -> emptyList()
            }

            _uiState.update {
                it.copy(
                    post      = post,
                    comments  = comments,
                    isLoading = false
                )
            }
        }
    }

    fun retry() = loadDetail()
}