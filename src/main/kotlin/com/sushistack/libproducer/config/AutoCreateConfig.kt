package com.sushistack.libproducer.config

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

@Configuration
class AutoCreateConfig {

    @Value("\${spring.kafka.topic}")
    lateinit var topic: String


    @Bean
    fun libraryEvents(): NewTopic =
        TopicBuilder
            .name(topic)
            .partitions(3)
            .replicas(3)
            .build()


}