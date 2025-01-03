package com.example.thread_client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
public class ClientController {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
    private final RestClient restClient;

    public ClientController(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("http://localhost:8085").build();
    }

    @GetMapping("/block/{seconds}")
    public String client(@PathVariable int seconds) {

        ResponseEntity<Void> result =  restClient.get()
                .uri("/block/{seconds}", seconds)
                .retrieve()
                .toBodilessEntity();

        logger.info("{} with body {} on {}", result.getStatusCode(), result.getBody(), Thread.currentThread().getName());
        return Thread.currentThread().toString();
    }
}
