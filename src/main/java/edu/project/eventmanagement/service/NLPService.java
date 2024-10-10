package edu.project.eventmanagement.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.json.JSONObject;

@Service
public class NLPService {

    private final String FLASK_SERVICE_URL = "http://localhost:5000/process_text";

    public String analyzeText(String text) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        JSONObject request = new JSONObject();
        request.put("text", text);

        HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);
        ResponseEntity<String> response = restTemplate.exchange(
                FLASK_SERVICE_URL, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }
}
