package com.example.googleapi.model
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
            "https://via.placeholder.com/128x190.png?text=Not+Found"
        } else {
            url.replace("http://", "https://").replace("&edge=curl", "")
        }
    }
}

data class ImageLinks(val smallThumbnail: String?, val thumbnail: String?)
