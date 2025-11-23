# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**streamViewService** is a RESTful web service that analyzes video streams based on manifest URLs. The service accepts a manifest URL as a POST parameter and returns JSON with video stream information.

## Key Architecture Goals

- **Single Docker Container**: All functionality packaged in one container
- **RESTful API**: POST endpoint accepting manifest URLs
- **Built-in API Documentation**: Self-documenting API accessible via the service
- **Static Web UI**: Simple webpage for testing API requests
- **Comprehensive Testing**: Both unit and integration tests

## Development Approach

This application is being built incrementally using Claude Code's Planning Mode. When implementing features:

1. Start by planning the architecture before writing code
2. Consider how each component will fit into the Docker container
3. Design the API structure to support self-documentation
4. Plan for testability at both unit and integration levels

## Architecture

**Technology Stack:**
- Spring Boot 3.2.0
- Java 17
- Maven for build management
- Springdoc OpenAPI for API documentation
- Docker for containerization

**Key Components:**
- `ManifestController`: REST API endpoint at POST /
- `HealthController`: Health check endpoint at GET /health
- `ManifestAnalyzerImpl`: Service for detecting HLS (.m3u8) and DASH (.mpd) manifests
- Static Web UI at /
- OpenAPI documentation at /apidocs

## Development Commands

**Build and test:**
```bash
mvn clean test           # Run all tests
mvn clean install        # Build JAR file
```

**Run locally:**
```bash
mvn spring-boot:run      # Start on port 8080
```

**Docker:**
```bash
docker build -t streamviewservice:latest .
docker run -p 8080:8080 streamviewservice:latest
```

## API Endpoints

- `POST /` - Analyze manifest URL, returns JSON with streamtype (hls, dash, or invalid)
- `GET /health` - Health check
- `GET /apidocs` - API documentation (Swagger UI)
- `GET /` - Static web UI for testing

## Testing

The test suite includes:
- Unit tests for controllers (5 tests)
- Unit tests for service layer (8 tests)
- All tests validate HLS, DASH, and invalid manifest handling
- Test data available in `src/test/resources/test-manifests.json`
