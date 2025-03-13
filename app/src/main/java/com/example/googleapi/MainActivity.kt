package com.example.googleapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.example.googleapi.ui.theme.BookSearchScreen
import com.example.googleapi.viewmodel.BooksViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                //ganti true flase untuk pake api asli atau layanan plasu
                BookSearchScreen(viewModel = BooksViewModel(useFakeRepo = false))
            }
        }

    }
        }

