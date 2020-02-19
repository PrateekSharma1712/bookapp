package com.prateek.booksapp.framework.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BooksResponseDTO(
    val count: Int?,
    val next: String?,
    val previous: Any?,
    @Json(name = "results")
    val books: List<Book?>?
)