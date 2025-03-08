package com.example.googleapi.ui.theme
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.googleapi.viewmodel.BooksViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookSearchScreen(viewModel: BooksViewModel = viewModel()) {
    var query by remember { mutableStateOf("Jazz History") }
    var showSearch by remember { mutableStateOf(false) }

    val books by viewModel.books.collectAsState()


    LaunchedEffect(Unit) { viewModel.searchBooks(query) }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFE8F5E9))
            .padding(16.dp)
    ) {
        Header(showSearch) { showSearch = !showSearch }

        AnimatedVisibility(
            visible = showSearch,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = {
                    query = it
                    viewModel.searchBooks(it)
                },
                label = { Text("Search Books", color = Color(0xFF388E3C)) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(books) { book -> BookItem(book) }
        }
    }
}