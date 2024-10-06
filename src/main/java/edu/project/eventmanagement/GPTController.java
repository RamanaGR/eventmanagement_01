package edu.project.eventmanagement;

import edu.project.eventmanagement.service.ContentGeneratorService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class GPTController {

    @Autowired
    private ContentGeneratorService contentGeneratorService;

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello from Spring Boot API!";
    }

    @GetMapping("/generate-content")
    public Mono<ResponseEntity<String>> generateContent(@RequestParam String eventType, @RequestParam String details) {
        String prompt = buildPrompt(eventType, details);
        return contentGeneratorService.generateContent(prompt)
                .map(content -> ResponseEntity.ok(content));
    }

    private String buildPrompt(String eventType, String details1) {
        String details = StringEscapeUtils.escapeJson(details1);
        return switch (eventType.toLowerCase()) {
            case "invitation" -> "Generate an invitation for the following event: " + details;
            case "timetable" -> "Create a timetable for the event with the following details: " + details;
            case "follow-up" -> "Write a follow-up email after the event: " + details;
            default -> "Generate content for the event: " + details;
        };
    }

}
