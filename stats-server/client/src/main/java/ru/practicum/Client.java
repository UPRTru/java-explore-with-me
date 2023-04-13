package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RequiredArgsConstructor
public class Client {
    protected final RestTemplate rest;

    protected <T> void post(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<T> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<Object> shareitServerResponse;
        try {
            shareitServerResponse = rest.exchange("/hit", HttpMethod.POST, requestEntity, Object.class);
        } catch (HttpStatusCodeException e) {
            ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
            return;
        }
        prepareGatewayResponse(shareitServerResponse);
    }

    private static void prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return;
        }
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());
        if (response.hasBody()) {
            responseBuilder.body(response.getBody());
            return;
        }
        responseBuilder.build();
    }
}