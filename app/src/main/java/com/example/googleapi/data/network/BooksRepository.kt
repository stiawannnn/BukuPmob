package com.example.googleapi.data.network
import com.example.googleapi.model.VolumeInfo

class BooksRepository(
    private val apiService: GoogleBooksService?,
    private val useFakeRepo: Boolean = false
) {
    suspend fun searchBooks(query: String): List<VolumeInfo> {
        return if (useFakeRepo) {
            fakeBooks()
        } else {
            apiService?.searchBooks(query)?.items?.map { it.volumeInfo } ?: emptyList()
        }
    }

    private fun fakeBooks(): List<VolumeInfo> {
        return FakeBooksRepo.getFakeBooks()
    }
}
