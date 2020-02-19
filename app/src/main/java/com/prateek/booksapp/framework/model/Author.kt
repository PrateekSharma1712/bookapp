package com.prateek.booksapp.framework.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Author(
    @Json(name = "birth_year")
    val birthYear: Int?,
    @Json(name = "death_year")
    val deathYear: Int?,
    @Json(name = "name")
    val name: String?
)