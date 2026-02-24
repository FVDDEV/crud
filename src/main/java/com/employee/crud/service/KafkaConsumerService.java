package com.employee.crud.service;

import com.employee.crud.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final UserService userService;

    @KafkaListener(
            topics = "KafkaTestDTOExample",
            containerFactory = "kafkaListenerFactory"
    )
    public void consumeDTO(UserDTO userDTO, Acknowledgment ack) {
        try {
            log.info("Consumed message: {}", userDTO);
            userService.createUser(userDTO);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error processing message", e);
            throw e;
        }
    }

    @KafkaListener(
            topics = "KafkaTestDTOExample.DLT",
            groupId = "dlt-consumer-group",
            containerFactory = "kafkaListenerFactory"
    )
    public void listenDLT(UserDTO userDTO) {
        log.warn("Received message in DLT: {}", userDTO);
    }
}