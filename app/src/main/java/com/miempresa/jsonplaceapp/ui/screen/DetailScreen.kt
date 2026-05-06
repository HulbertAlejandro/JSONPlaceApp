package com.miempresa.jsonplaceapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miempresa.jsonplaceapp.ui.components.CommentItem
import com.miempresa.jsonplaceapp.ui.viewmodel.DetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(viewModel: DetailViewModel) {
    val state by viewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text("Detalle de Post", maxLines = 1) },
                navigationIcon = { /* Icono volver si tienes nav */ },
                scrollBehavior = scrollBehavior
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
                item {
                    state.post?.let { post ->
                        Surface(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Column(Modifier.padding(20.dp)) {
                                Text("Publicación #${post.id}", style = MaterialTheme.typography.labelLarge)
                                Spacer(Modifier.height(8.dp))
                                Text(post.title.uppercase(), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                                HorizontalDivider(Modifier.padding(vertical = 12.dp), thickness = 0.5.dp)
                                Text(post.body, style = MaterialTheme.typography.bodyLarge, lineHeight = 24.sp)
                            }
                        }
                    }
                }

                item {
                    Text("Comentarios de la comunidad",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp))
                }

                items(state.comments) { comment ->
                    Card(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text(comment.email, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                            Text(comment.body, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}