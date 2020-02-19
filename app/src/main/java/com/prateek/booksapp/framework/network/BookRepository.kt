package com.prateek.booksapp.framework.network

import com.prateek.booksapp.framework.GutendexService
import com.prateek.booksapp.framework.model.Book
import javax.inject.Inject

class BookRepository @Inject constructor(private val gutendexService: GutendexService) {

    suspend fun fetchBooks(pageCount: Int = 1) : Pair<List<Book?>, String?> {

        val bookResponseDTO = gutendexService.fetchBooksAsync(pageCount).await()

        if (bookResponseDTO.isSuccessful) {
            bookResponseDTO.body()
        }

        return Pair(bookResponseDTO.body()?.books ?: emptyList(), bookResponseDTO.body()?.next)
    }

}

