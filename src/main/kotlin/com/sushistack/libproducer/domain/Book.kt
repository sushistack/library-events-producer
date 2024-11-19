package com.sushistack.libproducer.domain

import jakarta.validation.constraints.NotBlank

data class Book (
    val bookId: Int?,
    @field:NotBlank
    val bookName: String,
    @field:NotBlank
    val bookAuthor: String
)
