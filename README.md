
---

# Event Management

## Project Overview

The **Event Management** module is a Spring Boot-based application that leverages GPT (such as OpenAI's GPT models) to automatically produce customized content for events. This application generates invitations, timetables, and follow-up emails tailored to specific event requirements and exposes a RESTful API to interact with the module.

## Features

- **Generate Event Invitations**: Automatically create personalized invitations for events based on user-provided details.
- **Generate Event Timetables**: Automatically create event schedules/timetables based on the provided event details.
- **Generate Follow-Up Emails**: Automatically create professional follow-up emails after events.
- **Hello API**: A simple endpoint to return a greeting message.

## Technologies Used

- **Java 17**
- **Spring Boot 3.1.1**
    - Spring Web
    - Spring WebFlux (for non-blocking API calls)
- **Gradle** (Build tool)
- **OpenAI API** (for GPT-based content generation)
- **Jackson** (for JSON parsing)
- **WebClient** (for making HTTP requests to external APIs)

## System Requirements

- **Java 17** or higher
- **Gradle 7.0** or higher
- An **OpenAI API key** (or another GPT-based service API key)

---

## Setup Instructions

### Step 1: Clone the Repository

```bash
git clone https://github.com/RamanaGR/eventmanagement_01.git
cd event-management
```

### Step 2: Add OpenAI API Key

You will need an API key from OpenAI (or another GPT-based provider). Once you have the key, add it to your project.

- Open the `application.properties` file located at `src/main/resources/`.
- Add the following line with your actual OpenAI API key:

```properties
# src/main/resources/application.properties
openai.api.key=YOUR_API_KEY
```

> **Note:** Replace `YOUR_API_KEY` with your actual OpenAI API key.

### Step 3: Build and Run the Project

Build the project using Gradle:

```bash
./gradlew build
```

Once the build is successful, run the project:

```bash
./gradlew bootRun
```

The application will start on the default port **8080**.

---

## API Endpoints

The application exposes the following RESTful API endpoints:

### 1. **Hello API**

**Endpoint**: `/hello`  
**Method**: `GET`

#### Example Request

**cURL** command:

```bash
curl http://localhost:8080/hello
```

#### Example Response

```json
{
  "message": "Hello from Spring Boot API!"
}
```

### 2. **Generate Content API**

**Endpoint**: `/generate-content`  
**Method**: `GET`

#### Request Parameters

| Parameter | Type   | Description                                              | Example                               |
|-----------|--------|----------------------------------------------------------|---------------------------------------|
| `eventType`   | String | Type of content to generate (`invitation`, `timetable`, `follow-up`) | invitation                            |
| `details`  | String | The specific details of the event                       | Birthday party on 21st October at 5PM |

#### Example Request

**cURL** command:

```bash
curl "http://localhost:8080/generate-content?eventType=invitation&details=Birthday party on 21st October at 5PM"
```

#### Example Response

```json
{
  "content": "You're invited! Please join us for a Birthday party on 21st October at 5PM. We look forward to celebrating with you!"
}
```

---

## Application Structure

Here’s an overview of the key components of the project:

### 1. **WebClient Configuration**
The `WebClientConfig` class configures the WebClient instance used to make HTTP requests to the OpenAI API, retrieving the API key from the properties file.

```java
@Configuration
public class WebClientConfig {

    @Value("${openai.api.key}")
    private String apiKey;

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl("https://api.openai.com/v1")
                      .defaultHeader("Authorization", "Bearer " + apiKey)
                      .build();
    }
}
```

### 2. **Service Layer**
The `ContentGeneratorService` class contains the logic to interact with the GPT-based API and generate content.

```java
@Service
public class ContentGeneratorService {
    @Autowired
    private WebClient webClient;

    public Mono<String> generateContent(String prompt) {
        return webClient.post()
                        .uri("/completions")
                        .bodyValue(buildRequestBody(prompt))
                        .retrieve()
                        .bodyToMono(String.class);
    }

    private String buildRequestBody(String prompt) {
        return String.format("{\"model\": \"text-davinci-003\", \"prompt\": \"%s\", \"max_tokens\": 150}", prompt);
    }
}
```

### 3. **Controller Layer**
The `ContentController` class provides the REST API endpoints for generating content and the Hello API.

```java
@RestController
public class ContentController {

    @Autowired
    private ContentGeneratorService contentGeneratorService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello from Spring Boot API!");
    }

    @GetMapping("/generate-content")
    public Mono<ResponseEntity<String>> generateContent(@RequestParam String eventType, @RequestParam String details) {
        String prompt = buildPrompt(eventType, details);
        return contentGeneratorService.generateContent(prompt)
                .map(content -> ResponseEntity.ok(content));
    }

    private String buildPrompt(String eventType, String details) {
        return switch (eventType.toLowerCase()) {
            case "invitation" -> "Generate an invitation for the following event: " + details;
            case "timetable" -> "Create a timetable for the event with the following details: " + details;
            case "follow-up" -> "Write a follow-up email after the event: " + details;
            default -> "Generate content for the event: " + details;
        };
    }
}
```

### 4. **build.gradle**
Your `build.gradle` includes the necessary dependencies for Spring Web, WebClient, and testing.

```groovy
plugins {
    id 'org.springframework.boot' version '3.1.1'
    id 'io.spring.dependency-management' version '1.1.0'
    id 'java'
}

group = 'com.example'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '17'

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-webflux' // for WebClient
    implementation 'com.fasterxml.jackson.core:jackson-databind' // for JSON processing
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

---

## How It Works

1. **Client Requests**: The client can send a `GET` request to the `/hello` API to receive a greeting message or to the `/generate-content` API with `eventType` and `details` as query parameters.
2. **Content Generation**: For the `/generate-content` API, the `ContentGeneratorService` creates a request prompt and sends it to OpenAI's GPT model using WebClient.
3. **AI Response**: GPT returns the generated content based on the event type and details provided.
4. **Response to Client**: The generated content or greeting message is returned to the client in a JSON format.

---

## Future Enhancements

- **Add Customizable Event Types**: Allow users to add more event types and custom prompts.
- **Support for Multiple Languages**: Add support for generating content in different languages.
- **Advanced Formatting**: Enable formatting options for the generated content (HTML, Markdown, etc.).
- **Integration with Other APIs**: Integrate email or messaging services (like SendGrid or Twilio) for direct communication.

---

## License

This project is licensed under the MIT License. You are free to modify and distribute this code as needed.

---

## Conclusion

This documentation provides a complete guide to setting up and running the **Event Management** module. The application demonstrates how to integrate a GPT model with a Spring Boot project to dynamically generate event-related content through a RESTful API and a simple greeting API.

---
