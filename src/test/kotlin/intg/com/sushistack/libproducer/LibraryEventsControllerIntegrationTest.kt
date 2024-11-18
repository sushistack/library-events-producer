package com.sushistack.libproducer

import com.sushistack.libproducer.domain.LibraryEvent
import com.sushistack.libproducer.util.TestUtil
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.context.TestPropertySource

@EmbeddedKafka(
    topics = ["library-events"],
    partitions = 3
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = [
    "spring.kafka.producer.bootstrap-servers=\${spring.embedded.kafka.brokers}",
    "spring.kafka.admin.properties.bootstrap.servers=\${spring.embedded.kafka.brokers}"
])
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
        // 2024-11-18T17:53:20.098+09:00  INFO 52036 --- [library-events-producer] [ucer-producer-1] c.s.l.producer.LibraryEventsProducer     : Message sent successfully for the key := 0 and value := {"libraryEventId":null,"libraryEventType":"NEW","book":{"bookId":123,"bookName":"Dilip","bookAuthor":"Kafka Using Spring Boot"}} and partition := 2
    }
}