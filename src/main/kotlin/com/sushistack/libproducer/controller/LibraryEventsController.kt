package com.sushistack.libproducer.controller

import com.sushistack.libproducer.domain.LibraryEvent
import com.sushistack.libproducer.producer.LibraryEventsProducer
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class LibraryEventsController(private val libraryEventsProducer: LibraryEventsProducer) {

    private var log = KotlinLogging.logger {}

    @PostMapping("/v1/libraryevent")
    fun postLibraryEvents(@RequestBody libraryEvent: LibraryEvent): ResponseEntity<LibraryEvent> {
        log.info { "libraryEvent : $libraryEvent" }

        // libraryEventsProducer.sendLibraryEvent(libraryEvent)
        libraryEventsProducer.sendLibraryEventApproach3(libraryEvent)
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent)
    }

}