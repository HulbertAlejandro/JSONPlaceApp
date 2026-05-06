package com.miempresa.jsonplaceapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.miempresa.jsonplaceapp.domain.model.User
import com.miempresa.jsonplaceapp.ui.viewmodel.MainViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onPostClick: (Int) -> Unit
) {
    // Observación de datos
    val posts = viewModel.pagedPosts.collectAsLazyPagingItems()
    val uiState by viewModel.uiState.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()
    val filterState by viewModel.filterState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("JSONPlaceApp", fontWeight = FontWeight.ExtraBold)
                },
                actions = {
                    // Indicador de conexión sutil
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (isOnline) Color(0xFF4CAF50) else Color(0xFFF44336),
                            modifier = Modifier.size(10.dp)
                        ) {}
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = if (isOnline) "Online" else "Offline",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        // Usamos una Box normal para evitar errores de PullToRefresh si tu versión es antigua
        // Pero mantenemos el diseño premium
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Buscador estilizado (Material 3)
            OutlinedTextField(
                value = filterState.titleQuery,
                onValueChange = { viewModel.filterByTitle(it) },
                placeholder = { Text("Buscar publicaciones...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            // Chips de filtrado (Scroll Horizontal)
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                item {
                    FilterChip(
                        selected = filterState.userId == null,
                        onClick = { viewModel.filterByUserId(null) },
                        label = { Text("Todos") },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                items(uiState.users) { user: User ->
                    FilterChip(
                        selected = filterState.userId == user.id,
                        onClick = { viewModel.filterByUserId(user.id) },
                        label = { Text(user.name) },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // Lista de Posts con diseño de Tarjetas Elevadas
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(
                    count = posts.itemCount,
                    key = posts.itemKey { it.id },
                    contentType = posts.itemContentType { "posts" }
                ) { index ->
                    posts[index]?.let { post ->
                        ElevatedCard(
                            onClick = { onPostClick(post.id) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = post.title.replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                                    },
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.height(6.dp))
                                Text(
                                    text = post.body,
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 2,
                                    lineHeight = 20.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}