package com.example.googleapi.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googleapi.data.network.BooksRepository
import com.example.googleapi.data.network.RetrofitInstance
import com.example.googleapi.model.VolumeInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BooksViewModel(
    useFakeApi: Boolean = false
) : ViewModel() {

    private val repository = BooksRepository(
        apiService = if (useFakeApi) null else RetrofitInstance.api,
        useFakeApi = useFakeApi
    )

    private val _books = MutableStateFlow<List<VolumeInfo>>(emptyList())
    val books = _books.asStateFlow()

    private val defaultQuery = "Jazz History"

    init {
        fetchBooks()
    }

    private fun fetchBooks() {
        viewModelScope.launch {
            try {
                _books.value = repository.searchBooks(defaultQuery)
            } catch (e: Exception) {
                Log.e("API_ERROR", "Failed to fetch books: ${e.message}")
                _books.value = emptyList()
            }
        }
    }

    fun searchBooks(query: String) {
        viewModelScope.launch {
            try {
                _books.value = repository.searchBooks(query)
            } catch (e: Exception) {
                Log.e("API_ERROR", "Failed to fetch books: ${e.message}")
                _books.value = emptyList()
            }
        }
    }
}
