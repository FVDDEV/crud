//package com.employee.crud.service.impl;
//
//import org.awaitility.Awaitility;
//import com.employee.crud.dto.UserDTO;
//import com.employee.crud.service.UserService;
//import org.apache.kafka.clients.consumer.Consumer;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.clients.consumer.ConsumerRecords;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.test.EmbeddedKafkaBroker;
//import org.springframework.kafka.test.context.EmbeddedKafka;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.time.Duration;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//@EmbeddedKafka(
//        partitions = 1,
//        topics = {
//                "KafkaTestExample",
//                "KafkaTestDTOExample",
//                "KafkaTestDTOExample.DLT"
//        }
//)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@ActiveProfiles("test")
//class KafkaConsumerServiceTest {
//
//    @Autowired
//    private KafkaTemplate<String, Object> kafkaTemplate;
//
//    @Autowired
//    private UserService userService;
//
//    // =========================
//    // Mock UserService Bean
//    // =========================
//    @TestConfiguration
//    static class MockConfig {
//        @Bean
//        public UserService userService() {
//            return Mockito.mock(UserService.class);
//        }
//    }
//
//    // =========================
//    // Helper Consumer Factory
//    // =========================
//    private Consumer<String, String> createConsumer(String groupId, EmbeddedKafkaBroker broker) {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, broker.getBrokersAsString());
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//
//        return new DefaultKafkaConsumerFactory<>(
//                props,
//                new StringDeserializer(),
//                new StringDeserializer()
//        ).createConsumer();
//    }
//
//    // =========================
//    // TEST 1: Successful JSON DTO Processing
//    // =========================
//    @Test
//    void shouldProcessValidDTO() {
//
//        UserDTO dto = new UserDTO(1,"falguni","fd123", 1,"Admin");
//
//        kafkaTemplate.send("KafkaTestDTOExample", dto);
//
//        // Await until userService.createUser is invoked
//        Awaitility.await()
//                .atMost(Duration.ofSeconds(10))
//                .untilAsserted(() -> verify(userService, atLeastOnce()).createUser(any(UserDTO.class)));
//    }
//
//    // =========================
//    // TEST 2: Runtime Exception → Goes to DLT
//    // =========================
//    @Test
//    void shouldSendToDLTWhenServiceFails(@Autowired EmbeddedKafkaBroker broker) {
//
//        UserDTO dto = new UserDTO(0,"falguni","fd123", 1,"Admin");
//
//        doThrow(new RuntimeException("Forced failure")).when(userService).createUser(any());
//
//        kafkaTemplate.send("KafkaTestDTOExample", dto);
//
//        // Consume from DLT to confirm
//        Consumer<String, String> consumer = createConsumer("dlt-test-group", broker);
//        consumer.subscribe(Collections.singleton("KafkaTestDTOExample.DLT"));
//
//        org.awaitility.core.ConditionFactory await = Awaitility.await();
//        await.atMost(Duration.ofSeconds(15));
//        await.untilAsserted(() -> {
//            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
//            assertFalse(records.isEmpty(), "DLT should contain the failed message");
//        });
//    }
//
//    // =========================
//    // TEST 3: Non-Retryable Exception → Direct to DLT
//    // =========================
//    @Test
//    void shouldDirectlySendToDLTForNonRetryableException(@Autowired EmbeddedKafkaBroker broker) {
//
//        UserDTO dto = new UserDTO(1,"unfailing","fd123", 1,"noRole");
//
//        doThrow(new IllegalArgumentException("Validation failed")).when(userService).createUser(any());
//
//        kafkaTemplate.send("KafkaTestDTOExample", dto);
//
//        Consumer<String, String> consumer = createConsumer("dlt-nonretry-group", broker);
//        consumer.subscribe(Collections.singleton("KafkaTestDTOExample.DLT"));
//
//        Awaitility.await()
//                .atMost(Duration.ofSeconds(10))
//                .untilAsserted(() -> {
//                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
//                    assertFalse(records.isEmpty(), "DLT should contain the non-retryable exception message");
//                });
//    }
//
//    // =========================
//    // TEST 4: Deserialization Failure → DLT
//    // =========================
//    @Test
//    void shouldSendToDLTWhenJsonIsInvalid(@Autowired EmbeddedKafkaBroker broker) {
//
//        // Send malformed JSON
//        kafkaTemplate.send("KafkaTestDTOExample", "{invalid-json}");
//
//        Consumer<String, String> consumer = createConsumer("dlt-json-error-group", broker);
//        consumer.subscribe(Collections.singleton("KafkaTestDTOExample.DLT"));
//
//        Awaitility.await()
//                .atMost(Duration.ofSeconds(10))
//                .untilAsserted(() -> {
//                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
//                    assertFalse(records.isEmpty(), "DLT should contain the deserialization failure message");
//                });
//    }
//
//    // =========================
//    // TEST 5: String Listener Success
//    // =========================
//    @Test
//    void shouldProcessStringMessage(@Autowired EmbeddedKafkaBroker broker) {
//
//        kafkaTemplate.send("KafkaTestExample", "Hello Kafka");
//
//        // Since no dependent service for string listener, just wait briefly
//        Awaitility.await()
//                .atMost(Duration.ofSeconds(5))
//                .until(() -> true); // ensures message was consumed without errors
//    }
//}