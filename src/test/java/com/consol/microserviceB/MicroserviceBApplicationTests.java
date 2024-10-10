package com.consol.microserviceB;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@ActiveProfiles({"test"})
@Testcontainers
class MicroserviceBApplicationTests {

    @Container
    static PostgreSQLContainer<?> postgreSQL = new PostgreSQLContainer<>("postgres:12.7");
    static RabbitMQContainer rabbitMq = new RabbitMQContainer("rabbitmq:3");

    @BeforeAll
    public static void startContainers() {
        postgreSQL.start();
        rabbitMq.start();
    }

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {

        // Postgres
        registry.add("spring.datasource.url", postgreSQL::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQL::getUsername);
        registry.add("spring.datasource.password", postgreSQL::getPassword);

        // RabbitMQ
        registry.add("spring.rabbitmq.host", rabbitMq::getHost);
        registry.add("spring.rabbitmq.port", rabbitMq::getAmqpPort);
        registry.add("spring.rabbitmq.username", rabbitMq::getAdminUsername);
        registry.add("spring.rabbitmq.password", rabbitMq::getAdminPassword);

        // Telemetry
        registry.add("opentracing.jaeger.enabled", ()->"false");
        registry.add("opentracing.jaeger.udp-sender.host", ()->"observability-jaeger");
    }

    @Test
    void contextLoads() {
    }

    @AfterAll
    public static void stopContainers() {
        postgreSQL.stop();
        rabbitMq.stop();
    }

}
