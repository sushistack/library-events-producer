package com.sushistack.libproducer.domain

data class LibraryEvent(
    val libraryEventId: Long?,
    val libraryEventType: LibraryEventType,
    val book: Book
)
