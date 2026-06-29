package com.vinesh.banking.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TransactionConsumer {

    private static final Logger log = LoggerFactory.getLogger(TransactionConsumer.class);

    @KafkaListener(topics = "transaction-events", groupId = "banking-group")
    public void consume(String message) {
        log.info("Received transaction event: {}", message);

        // TODO: deserialize into TransactionEvent and route based on type
        //  e.g. DEPOSIT -> push notification, WITHDRAWAL -> fraud check, etc.
        //  for now just logging so we can verify the pipeline end-to-end
    }
}
