package com.sushistack.libproducer

import com.sushistack.libproducer.domain.LibraryEvent
import com.sushistack.libproducer.util.TestUtil
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LibraryEventsControllerIntegrationTest {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate


    @Test
    fun postLibraryEvents() {
        // given
        val httpHeaders = HttpHeaders()
        httpHeaders.set("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        val httpEntity = HttpEntity(TestUtil.libraryEventRecord(), httpHeaders)

        // when
        val responseEntity = testRestTemplate
            .exchange("/v1/libraryevent", HttpMethod.POST, httpEntity, LibraryEvent::class.java)

        // then
        Assertions.assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.CREATED)
    }
}