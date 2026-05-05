package com.miempresa.jsonplaceapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.miempresa.jsonplaceapp.ui.components.CommentItem
import com.miempresa.jsonplaceapp.ui.viewmodel.DetailViewModel

@Composable
fun DetailScreen(viewModel: DetailViewModel) {
    val state by viewModel.uiState.collectAsState()

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }
        return
    }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        item {
            state.post?.let { post ->
                Text(post.title, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text(post.body)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        item {
            Text("Comentarios", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(state.comments) { comment ->
            CommentItem(comment)
        }
    }
}