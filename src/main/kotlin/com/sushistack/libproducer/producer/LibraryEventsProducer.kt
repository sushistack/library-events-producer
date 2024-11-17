package com.sushistack.libproducer.producer

import com.fasterxml.jackson.databind.ObjectMapper
import com.sushistack.libproducer.domain.LibraryEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class LibraryEventsProducer(
    private val kafkaTemplate: KafkaTemplate<Long, String>,
    private val objectMapper: ObjectMapper
) {

    private var log = KotlinLogging.logger {}

    @Value("\${spring.kafka.topic}")
    lateinit var topic: String

    fun sendLibraryEvent(libraryEvent: LibraryEvent): CompletableFuture<SendResult<Long, String>> {
        val key = libraryEvent.libraryEventId ?: 0L
        val value = objectMapper.writeValueAsString(libraryEvent)

        // This is a blocking call
        val cf = kafkaTemplate.send(topic, key, value)
        return cf.whenComplete { sendResult, throwable ->
            if (throwable != null) {
                handleFailure(key, value, throwable)
            } else {
                handleSuccess(key, value, sendResult)
            }
        }
    }

    private fun handleFailure(key: Long, value: String, throwable: Throwable) {
        log.error { "Error sending the message and the exception is ${throwable.message}" }
    }

    private fun handleSuccess(key: Long, value: String, sendResult: SendResult<Long, String>) {
        log.info { "Message sent successfully for the key := $key and value := $value and partition := ${sendResult.recordMetadata.partition()}" }
    }
}