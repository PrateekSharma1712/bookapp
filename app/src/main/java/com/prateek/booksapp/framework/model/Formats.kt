package com.prateek.booksapp.framework.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Formats(
    @Json(name = "text/html")
    val html: String?,
    @Json(name = "text/html; charset=utf-8")
    val htmlCharsetUTF8: String?,
    @Json(name = "text/html; charset=iso-8859-1")
    val htmlCharsetISO: String?,
    @Json(name = "text/html; charset=us-ascii")
    val htmlCharsetUSAscii: String?,

    @Json(name = "application/pdf")
    val pdf: String?,

    @Json(name = "text/plain")
    val text: String?,
    @Json(name = "text/plain; charset=us-ascii")
    val textCharsetUSAscii: String?,
    @Json(name = "text/plain; charset=utf-8")
    val textCharsetUTF8: String?,
    @Json(name = "text/plain; charset=iso-8859-1")
    val textCharsetISO: String?,

    @Json(name = "image/jpeg")
    val imageURL: String?
)