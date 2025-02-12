# Ollama Chat API with Spring Boot

This project integrates Ollama, a powerful AI model, with Spring Boot to create a REST API for generating intelligent chat responses. It supports both single responses and real-time streaming for dynamic interactions.

## Features
- **Single Response**: Generate a quick reply for simple queries.
- **Streaming Support**: Stream responses in real-time for dynamic conversations.
- **Spring Boot Integration**: Built with Spring Boot for scalability and ease of use.

## Technologies Used
- **Spring Boot**: For building the REST API.
- **Ollama**: For AI-powered text generation.
- **Reactive Programming**: For streaming responses efficiently.

## Prerequisites
- Java 17 or higher
- Maven
- Ollama server running locally (default: http://localhost:11434)

## Setup
### Clone the Repository:
```bash
git clone https://github.com/your-username/ollama-springboot-chat.git
cd ollama-springboot-chat
```

### Build the Project:
```bash
mvn clean install
```

### Run the Application:
```bash
mvn spring-boot:run
```
The application will start on http://localhost:8080.

### Ensure Ollama is Running:
Make sure the Ollama server is running locally at http://localhost:11434.

## API Endpoints
### 1. Generate a Single Response
- **Endpoint**: `GET /ai/generate`
- **Description**: Generates a single response for a given message.
- **Parameters**:
  - `message` (optional): The input message (default: "Tell me a joke").
- **Example Request**:
```bash
curl -X GET "http://localhost:8080/ai/generate?message=What%20is%20the%20capital%20of%20France?"
```
- **Example Response**:
```json
{
  "generation": "The capital of France is Paris."
}
```

### 2. Stream Responses
- **Endpoint**: `GET /ai/generateStream`
- **Description**: Streams responses in real-time for a given message.
- **Parameters**:
  - `message` (optional): The input message (default: "Tell me a joke").
- **Example Request**:
```bash
curl -X GET "http://localhost:8080/ai/generateStream?message=What%20is%20the%20capital%20of%20France?"
```
- **Example Response (streaming)**:
```json
"The"
"capital"
"of"
"France"
"is"
"Paris."
```

## Example Use Cases
### Single Response
- **Input**: "Tell me a joke"
- **Response**:
```json
{
  "generation": "Why don't scientists trust atoms? Because they make up everything!"
}
```

### Streaming Response
- **Input**: "Explain the solar system"
- **Response (streaming)**:
```text
"The"
"solar"
"system"
"consists"
"of"
"the"
"Sun"
"and"
"the"
"planets"
"that"
"orbit"
"it."
```

## Configuration
You can customize the Ollama model and server URL in the `application.properties` file:
```properties
# Ollama configuration
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.model=smollm
```

## What’s Next?
- Add support for multi-turn conversations.
- Enhance the AI model with custom training data.
- Build a frontend for a more interactive chat experience.

## Contributing
Contributions are welcome! If you’d like to contribute, please:
1. Fork the repository.
2. Create a new branch for your feature or bugfix.
3. Submit a pull request.

## License
This project is licensed under the MIT License. See the `LICENSE` file for details.

