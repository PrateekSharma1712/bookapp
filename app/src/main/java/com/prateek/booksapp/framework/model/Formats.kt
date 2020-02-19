package com.prateek.booksapp.framework.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Formats(
    @Json(name = "application/epub+zip")
    val applicationepubzip: String?,
    @Json(name = "application/rdf+xml")
    val applicationrdfxml: String?,
    @Json(name = "application/x-mobipocket-ebook")
    val applicationxMobipocketEbook: String?,
    @Json(name = "application/zip")
    val applicationzip: String?,
    @Json(name = "text/html; charset=utf-8")
    val texthtmlCharsetutf8: String?,
    @Json(name = "text/plain; charset=us-ascii")
    val textplainCharsetusAscii: String?,
    @Json(name = "text/plain; charset=utf-8")
    val textplainCharsetutf8: String?
)