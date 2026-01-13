package com.golfapp.igrisce.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class TurnirjiClient {

    private final RestTemplate restTemplate;

    @Value("${turnirji.service.url:http://turnirji:8080}")
    private String turnirjiServiceUrl;

    public boolean isTournamentOnDate(LocalDate datum) {
        try {
            String url = turnirjiServiceUrl + "/api/turnirji/check-date?datum=" + datum.toString();
            Boolean result = restTemplate.getForObject(url, Boolean.class);
            return result != null && result;
        } catch (Exception e) {
            System.err.println("Failed to check tournament on date: " + e.getMessage());
            return false; // If service is down, allow reservations
        }
    }
}
