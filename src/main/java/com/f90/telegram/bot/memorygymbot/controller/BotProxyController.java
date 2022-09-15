package com.f90.telegram.bot.memorygymbot.controller;

import com.f90.telegram.bot.memorygymbot.exception.InternalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping(path = "")
@Slf4j
public class BotProxyController {

    private final RestTemplate restTemplate;

    public BotProxyController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping("/**")
    public ResponseEntity<String> mirrorRest(@RequestBody(required = false) String body, HttpMethod method, HttpServletRequest request) throws URISyntaxException {
        log.info("mirrorRest() - msg: received http requiest; method={}, uri={}", method, request.getRequestURI());
        URI uri = new URI("http", null, "localhost", 8090, "/callback/webhook", request.getQueryString(), null);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        try {
            return restTemplate.exchange(uri, method, entity, String.class);
        } catch (Exception ex) {
            log.error("mirrorRest() - msg: error during proxy request to TelegramBot WebHook.", ex);
            throw new InternalException("Error during proxy request to TelegramBot WebHook: " + ex.getMessage());
        }
    }
}
