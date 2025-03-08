package com.example.googleapi
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// === Retrofit API Service ===
interface GoogleBooksService {
    @GET("volumes")
    suspend fun searchBooks(@Query("q") query: String): GoogleBooksResponse
}

// === Retrofit Instance ===
object RetrofitInstance {
    val api: GoogleBooksService by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/books/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GoogleBooksService::class.java)
    }
}

// === Model Data ===
data class GoogleBooksResponse(val items: List<GoogleBookItem>?)
data class GoogleBookItem(val volumeInfo: VolumeInfo)
data class VolumeInfo(
    val title: String,
    val authors: List<String>?,
    val imageLinks: ImageLinks?
) {
    fun getCoverUrl(): String {
        val url = imageLinks?.thumbnail ?: imageLinks?.smallThumbnail
        return if (url.isNullOrEmpty()) {
            "https://via.placeholder.com/128x190.png?text=Not+Found" // 🚨 Gambar Not Found jika tidak tersedia
        } else {
            url.replace("http://", "https://") // ✅ Pastikan HTTPS
                .replace("&edge=curl", "") // ✅ Hapus parameter tambahan
        }
    }
}

data class ImageLinks(val smallThumbnail: String?, val thumbnail: String?)

// === ViewModel ===
class BooksViewModel : ViewModel() {
    var books by mutableStateOf<List<VolumeInfo>>(emptyList())

    init {
        searchBooks("jazz history") //
    }

    fun searchBooks(query: String) {
        if (query.length < 3) return
        viewModelScope.launch {
            books = try {
                RetrofitInstance.api.searchBooks(query).items?.map { it.volumeInfo } ?: emptyList()
            } catch (e: Exception) {
                Log.e("API_ERROR", "Failed to fetch books: ${e.message}")
                emptyList()
            }
        }
    }
}

// === UI - Pencarian & Grid Buku ===
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookSearchScreen(viewModel: BooksViewModel = viewModel()) {
    var query by remember { mutableStateOf("Jazz History") }
    var showSearch by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFE8F5E9)) // 💚 Background hijau muda
            .padding(16.dp)
    ) {
        // === Header ===
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF388E3C)) // 💚 Warna hijau gelap
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "BookShelf",
                fontSize = 24.sp,
                textAlign = TextAlign.Start,
                color = Color.White
            )
            IconButton(onClick = { showSearch = !showSearch }) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
            }
        }

        // === Search Box ===
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
                label = { Text("Search Books", color = Color(0xFF388E3C)) }, // 💚 Warna hijau
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF388E3C),
                    unfocusedBorderColor = Color(0xFF81C784),
                    cursorColor = Color(0xFF388E3C)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }

        // === Grid Buku ===
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(viewModel.books) { book -> BookItem(book) }
        }
    }
}

// === UI - Item Buku ===
@Composable
fun BookItem(book: VolumeInfo) {
    val imageUrl = book.getCoverUrl()
    Log.d("BookCover", "Book: ${book.title}, URL: $imageUrl") // Debug URL

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.7f),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = imageUrl, // 🔥 Menggunakan Coil untuk memuat gambar
                    error = rememberAsyncImagePainter("https://via.placeholder.com/128x190.png?text=Not+Found"), // ✅ Placeholder jika gagal
                    placeholder = rememberAsyncImagePainter("https://via.placeholder.com/128x190.png?text=Loading") // ⏳ Loading sementara
                ),
                contentDescription = book.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                book.title,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Text(
                book.authors?.joinToString(", ") ?: "Unknown Author",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }
    }
}

// === MainActivity ===
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                BookSearchScreen()
            }
        }
    }
}

