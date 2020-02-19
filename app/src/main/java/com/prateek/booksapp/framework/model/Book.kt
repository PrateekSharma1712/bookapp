package com.prateek.booksapp.framework.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Result(
    val authors: List<Author?>?,
    val bookshelves: List<String?>?,
    val downloadCount: Int?,
    val formats: Formats?,
    val id: Int?,
    val languages: List<String?>?,
    val mediaType: String?,
    val subjects: List<String?>?,
    val title: String?
)