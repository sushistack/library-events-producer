package com.sushistack.libproducer.controller

import com.fasterxml.jackson.core.JsonProcessingException
import com.sushistack.libproducer.domain.LibraryEvent
import com.sushistack.libproducer.domain.LibraryEventType
import com.sushistack.libproducer.producer.LibraryEventsProducer
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class LibraryEventsController(private val libraryEventsProducer: LibraryEventsProducer) {

    private var log = KotlinLogging.logger {}

    @PostMapping("/v1/libraryevent")
    fun postLibraryEvents(@Valid @RequestBody libraryEvent: LibraryEvent): ResponseEntity<LibraryEvent> {
        log.info { "libraryEvent : $libraryEvent" }

        // libraryEventsProducer.sendLibraryEvent(libraryEvent)
        libraryEventsProducer.sendLibraryEventApproach3(libraryEvent)
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent)
    }

    @PutMapping("/v1/libraryevent")
    @Throws(JsonProcessingException::class)
    fun putLibraryEvent(@RequestBody libraryEvent: @Valid LibraryEvent): ResponseEntity<*> {
        val badRequest = validateLibraryEvent(libraryEvent)
        if (badRequest != null) return badRequest

        libraryEventsProducer.sendLibraryEventApproach2(libraryEvent)
        log.info("after produce call")
        return ResponseEntity.status(HttpStatus.OK).body(libraryEvent)
    }

    private fun validateLibraryEvent(libraryEvent: LibraryEvent): ResponseEntity<String>? {
        if (libraryEvent.libraryEventId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please pass the LibraryEventId")
        }

        if (LibraryEventType.UPDATE != libraryEvent.libraryEventType) {
            log.info { "Inside the if block" }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only UPDATE event type is supported")
        }
        return null
    }
}