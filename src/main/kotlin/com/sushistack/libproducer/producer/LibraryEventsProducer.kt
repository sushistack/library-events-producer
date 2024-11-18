package com.sushistack.libproducer.producer

import com.fasterxml.jackson.databind.ObjectMapper
import com.sushistack.libproducer.domain.LibraryEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.internals.RecordHeader
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

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

        // 1. This is a blocking call - get metadata about the kafka cluster
        // 2. Send message happens - Returns a CompletableFuture
        val cf = kafkaTemplate.send(topic, key, value)
        return cf.whenComplete { sendResult, throwable ->
            if (throwable != null) {
                handleFailure(key, value, throwable)
            } else {
                handleSuccess(key, value, sendResult)
            }
        }
    }

    fun sendLibraryEventApproach2(libraryEvent: LibraryEvent): SendResult<Long, String> {
        val key = libraryEvent.libraryEventId ?: 0L
        val value = objectMapper.writeValueAsString(libraryEvent)

        // 1. This is a blocking call - get metadata about the kafka cluster
        // 2. Block and wait until the message is sent to the kafka
        val sendResult = kafkaTemplate.send(topic, key, value).get(3, TimeUnit.SECONDS)
        handleSuccess(key, value, sendResult)
        return sendResult
    }

    fun sendLibraryEventApproach3(libraryEvent: LibraryEvent): CompletableFuture<SendResult<Long, String>>? {
        val key = libraryEvent.libraryEventId ?: 0L
        val value = objectMapper.writeValueAsString(libraryEvent)

        val producerRecord = buildProducerRecord(key, value)

        // 1. This is a blocking call - get metadata about the kafka cluster
        // 2. Block and wait until the message is sent to the kafka
        val cf = kafkaTemplate.send(producerRecord)
        return cf.whenComplete { sendResult, throwable ->
            if (throwable != null) {
                handleFailure(key, value, throwable)
            } else {
                handleSuccess(key, value, sendResult)
            }
        }
    }

    private fun buildProducerRecord(key: Long, value: String): ProducerRecord<Long, String> {
        val recordHeaders = mutableListOf(RecordHeader("event-source", "scanner".toByteArray()))

        return ProducerRecord(topic, null, null, key, value, recordHeaders)
    }

    private fun handleFailure(key: Long, value: String, throwable: Throwable) {
        log.error { "Error sending the message and the exception is ${throwable.message}" }
    }

    private fun handleSuccess(key: Long, value: String, sendResult: SendResult<Long, String>) {
        log.info { "Message sent successfully for the key := $key and value := $value and partition := ${sendResult.recordMetadata.partition()}" }
    }
}