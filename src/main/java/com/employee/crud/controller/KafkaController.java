package com.employee.crud.controller;

import com.employee.crud.dto.UserDTO;
import com.employee.crud.service.KafkaProducerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/kafka")
@RequiredArgsConstructor
public class KafkaController {

    private final KafkaProducerService producerService;
    private final KafkaTemplate<String,UserDTO> kafkaTemplate;

    @GetMapping("/publish/{message}")
    public String publish(@PathVariable("message") String message) {
        log.info("Inside the publish method.");
        producerService.sendMessage("KafkaTestExample", message);
        return "Message sent successfully!";
    }

    @PostMapping
    public ResponseEntity<Void> createUserByKafka(@Valid @RequestBody UserDTO userDTO) {

        log.info("Inside createUserByKafka method.");

        try {
            kafkaTemplate.send("KafkaTestDTOExample", userDTO).get(); // wait for broker ack
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            log.error("Failed to send message to Kafka", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
