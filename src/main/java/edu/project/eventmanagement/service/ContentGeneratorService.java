package edu.project.eventmanagement.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class ContentGeneratorService {

    @Autowired
    private WebClient webClient;

    public Mono<String> generateContent(String prompt) {
        // Create the request body with the model parameter included
        String requestBody = String.format(
                "{\"model\": \"gpt-4-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"%s\"}], \"max_tokens\": 150}",
                prompt.replace("\"", "\\\"")  // Escape double quotes in the prompt
        );

        return webClient.post()
                .uri("/completions")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    System.err.println("Error response: " + e.getResponseBodyAsString());
                    return Mono.error(new RuntimeException("API call failed: " + e.getStatusCode()));
                });
    }
}
