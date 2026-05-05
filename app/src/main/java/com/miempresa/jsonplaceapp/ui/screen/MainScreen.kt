package com.miempresa.jsonplaceapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.miempresa.jsonplaceapp.ui.components.PostItem
import com.miempresa.jsonplaceapp.ui.viewmodel.MainViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onPostClick: (Int) -> Unit
) {
    val posts = viewModel.pagedPosts.collectAsLazyPagingItems()
    val uiState by viewModel.uiState.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()
    val filterState by viewModel.filterState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {

        // Indicador de conexión
        Surface(
            color = if (isOnline) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.errorContainer,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (isOnline) "🟢 Conectado" else "🔴 Offline",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.labelMedium
            )
        }

        // Buscador por título
        OutlinedTextField(
            value = filterState.titleQuery,
            onValueChange = { viewModel.filterByTitle(it) },
            label = { Text("Buscar por título") },
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            singleLine = true
        )

        // Dropdown usuarios
        var expanded by remember { mutableStateOf(false) }
        Box(modifier = Modifier.padding(8.dp)) {
            Button(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                val selectedUser = uiState.users.find { it.id == filterState.userId }
                Text(selectedUser?.let { "Usuario: ${it.name}" } ?: "Filtrar por usuario")
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(
                    text = { Text("Todos") },
                    onClick = { viewModel.filterByUserId(null); expanded = false }
                )
                uiState.users.forEach { user ->
                    DropdownMenuItem(
                        text = { Text(user.name) },
                        onClick = { viewModel.filterByUserId(user.id); expanded = false }
                    )
                }
            }
        }

        // 📜 LA LISTA (Sintaxis corregida para evitar conflictos de 'items')
        LazyColumn(modifier = Modifier.fillMaxSize()) {

            // Usamos items(posts.itemCount) para ser explícitos y evitar el error de Paging
            items(
                count = posts.itemCount,
                key = posts.itemKey { it.id },
                contentType = posts.itemContentType { "posts" }
            ) { index ->
                val post = posts[index]
                post?.let {
                    PostItem(
                        post = it,
                        onClick = { onPostClick(it.id) }
                    )
                }
            }

            // Manejo de carga
            val loadState = posts.loadState
            if (loadState.append is LoadState.Loading || loadState.refresh is LoadState.Loading) {
                item {
                    Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}