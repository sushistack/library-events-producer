package com.sushistack.libproducer.util

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.sushistack.libproducer.domain.Book
import com.sushistack.libproducer.domain.LibraryEvent
import com.sushistack.libproducer.domain.LibraryEventType

object TestUtil {
    fun bookRecord(): Book {
        return Book(123, "Dilip", "Kafka Using Spring Boot")
    }

    fun bookRecordWithInvalidValues(): Book {
        return Book(null, "", "Kafka Using Spring Boot")
    }

    fun libraryEventRecord(): LibraryEvent {
        return LibraryEvent(
            null,
            LibraryEventType.NEW,
            bookRecord()
        )
    }

    fun newLibraryEventRecordWithLibraryEventId(): LibraryEvent {
        return LibraryEvent(
            123,
            LibraryEventType.NEW,
            bookRecord()
        )
    }

    fun libraryEventRecordUpdate(): LibraryEvent {
        return LibraryEvent(
            123,
            LibraryEventType.UPDATE,
            bookRecord()
        )
    }

    fun libraryEventRecordUpdateWithNullLibraryEventId(): LibraryEvent {
        return LibraryEvent(
            null,
            LibraryEventType.UPDATE,
            bookRecord()
        )
    }

    fun libraryEventRecordWithInvalidBook(): LibraryEvent {
        return LibraryEvent(
            null,
            LibraryEventType.NEW,
            bookRecordWithInvalidValues()
        )
    }

    fun parseLibraryEventRecord(objectMapper: ObjectMapper, json: String?): LibraryEvent {
        try {
            return objectMapper.readValue(json, LibraryEvent::class.java)
        } catch (e: JsonProcessingException) {
            throw RuntimeException(e)
        }
    }
}