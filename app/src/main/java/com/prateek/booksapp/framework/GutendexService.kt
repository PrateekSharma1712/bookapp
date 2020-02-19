package com.prateek.booksapp.framework

import com.prateek.booksapp.framework.model.BooksResponseDTO
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GutendexService {

    /***
     * Fetches books from API having mime type as image/jpeg page wise
     */
    @GET("books")
    fun fetchBooksAsync(
        @Query("page") page: Int = 1, @Query(
            "mime_type",
            encoded = true
        ) mimeType: String = "image/jpeg"
    ): Deferred<Response<BooksResponseDTO>>
}