package com.sushistack.libproducer.domain

import jakarta.validation.Valid

data class LibraryEvent(
    val libraryEventId: Long?,
    val libraryEventType: LibraryEventType,
    @field:Valid
    val book: Book
)
