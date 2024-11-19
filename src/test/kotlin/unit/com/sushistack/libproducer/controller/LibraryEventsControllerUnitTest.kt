package com.sushistack.libproducer.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.sushistack.libproducer.producer.LibraryEventsProducer
import com.sushistack.libproducer.util.TestUtil
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [LibraryEventsController::class])
class LibraryEventsControllerUnitTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockkBean
    lateinit var libraryEventsProducer: LibraryEventsProducer

    @Test
    fun postLibraryEvents() {
        // Given
        val json = objectMapper.writeValueAsString(TestUtil.libraryEventRecord())

        // When
        every { libraryEventsProducer.sendLibraryEventApproach3(any()) } returns null

        // Then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/libraryevent")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated)
    }

    @Test
    fun postLibraryEventsInvalidValues() {
        // Given
        val json = objectMapper.writeValueAsString(TestUtil.libraryEventRecordWithInvalidBook())

        // When
        every { libraryEventsProducer.sendLibraryEventApproach3(any()) } returns null

        // Then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/libraryevent")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError)
    }


}