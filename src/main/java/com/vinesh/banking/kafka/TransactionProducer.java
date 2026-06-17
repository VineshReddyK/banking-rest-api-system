package com.vinesh.banking.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TransactionProducer {

    private static final Logger log = LoggerFactory.getLogger(TransactionProducer.class);
    private static final String TOPIC = "transaction-events";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public void publish(TransactionEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(TOPIC, event.getType(), payload);
            log.info("Published transaction event: {}", payload);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize transaction event: {}", e.getMessage());
        }
    }
}
