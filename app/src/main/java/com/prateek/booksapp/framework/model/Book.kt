package com.prateek.booksapp.framework.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Book(
    val authors: List<Author?>?,
    val bookshelves: List<String?>?,
    @Json(name = "download_count")
    val downloadCount: Int?,
    val formats: Formats?,
    val id: Int?,
    val languages: List<String?>?,
    @Json(name = "media_type")
    val mediaType: String?,
    val subjects: List<String?>?,
    val title: String?
)