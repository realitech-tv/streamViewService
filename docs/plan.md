# streamViewService - Incremental Development Plan

This document outlines the incremental development plan for building the streamViewService application. Each iteration delivers working, testable functionality that builds upon previous work.

## Iteration 1: Project Foundation & Health Check

### Objectives
- Set up Java Spring Boot project structure
- Implement health check endpoint
- Establish testing foundation

### Implementation Steps
1. Create `pom.xml` with Spring Boot dependencies (Web, Test, Actuator)
2. Create main application class
3. Implement `/health` endpoint controller
4. Create health response DTO
5. Write unit tests for health endpoint

### Validation Criteria
- [x] `mvn clean install` completes successfully
- [x] Application starts on port 8080
- [x] `curl http://localhost:8080/health` returns HTTP 200
- [x] Response is valid JSON with service status
- [x] All unit tests pass

### Test Commands
```bash
mvn clean test
mvn spring-boot:run
curl -v http://localhost:8080/health
```

---

## Iteration 2: Basic API Structure

### Objectives
- Implement POST / endpoint with basic structure
- Create request/response DTOs
- Return stub response

### Implementation Steps
1. Create `ManifestRequest` DTO with `url` field
2. Create `ManifestResponse` DTO matching response.schema.json
3. Implement `ManifestController` with POST / endpoint
4. Return hardcoded `{"streamtype": "invalid"}` for all requests
5. Add validation for URL parameter
6. Write unit tests for endpoint

### Validation Criteria
- [x] Endpoint accepts POST requests with JSON body containing `url`
- [x] Returns 200 with JSON response matching schema
- [x] Returns 400 for missing `url` parameter
- [x] Unit tests verify request/response structure
- [x] All tests pass

### Test Commands
```bash
mvn clean test
curl -X POST http://localhost:8080/ -H "Content-Type: application/json" -d '{"url":"https://example.com/test.m3u8"}'
```

---

## Iteration 3: HLS Manifest Detection

### Objectives
- Implement HLS manifest detection logic
- Parse .m3u8 files
- Return "hls" for valid HLS manifests

### Implementation Steps
1. Create `ManifestService` interface
2. Implement `HlsDetector` service
3. Add logic to detect .m3u8 file extension
4. Fetch URL content and validate HLS format (#EXTM3U header)
5. Update controller to use service
6. Write unit tests with mock HTTP responses
7. Test with real URLs from test-manifests.json

### Validation Criteria
- [x] Service detects .m3u8 URLs correctly
- [x] Service validates #EXTM3U header in content
- [x] Returns `{"streamtype": "hls"}` for valid HLS manifests
- [x] Handles query parameters in URLs (AR002)
- [x] Unit tests cover various HLS scenarios
- [x] Integration test with Radiant test stream succeeds

### Test Commands
```bash
mvn clean test
curl -X POST http://localhost:8080/ -H "Content-Type: application/json" -d '{"url":"https://www.radiantmediaplayer.com/media/rmp-segment/bbb-abr-aes/playlist.m3u8"}'
```

---

## Iteration 4: DASH Manifest Detection

### Objectives
- Implement DASH manifest detection logic
- Parse .mpd files
- Return "dash" for valid DASH manifests

### Implementation Steps
1. Implement `DashDetector` service
2. Add logic to detect .mpd file extension
3. Fetch URL content and validate MPD XML format
4. Look for `<MPD>` root element
5. Update service to check both HLS and DASH
6. Write unit tests with mock HTTP responses
7. Test with real URLs from test-manifests.json

### Validation Criteria
- [x] Service detects .mpd URLs correctly
- [x] Service validates MPD XML structure
- [x] Returns `{"streamtype": "dash"}` for valid DASH manifests
- [x] Handles query parameters in URLs (AR002)
- [x] Unit tests cover various DASH scenarios
- [x] Integration test with BBC and Shaka streams succeeds

### Test Commands
```bash
mvn clean test
curl -X POST http://localhost:8080/ -H "Content-Type: application/json" -d '{"url":"https://storage.googleapis.com/shaka-demo-assets/sintel-widevine/dash.mpd"}'
```

---

## Iteration 5: Invalid Manifest Handling

### Objectives
- Implement comprehensive validation logic
- Handle all edge cases and error scenarios
- Return "invalid" appropriately

### Implementation Steps
1. Add URL format validation
2. Handle HTTP errors (404, 500, timeouts)
3. Handle non-manifest file types (.mp4, .txt, etc.)
4. Handle malformed/corrupted manifests
5. Handle empty URLs
6. Add timeout configuration (e.g., 10 seconds)
7. Write unit tests for all invalid scenarios

### Validation Criteria
- [x] Returns `{"streamtype": "invalid"}` for malformed URLs
- [x] Returns `{"streamtype": "invalid"}` for 404 responses
- [x] Returns `{"streamtype": "invalid"}` for non-manifest files
- [x] Returns `{"streamtype": "invalid"}` for empty URLs
- [x] Returns `{"streamtype": "invalid"}` for corrupted manifests
- [x] Request times out after configured duration
- [x] All invalid test cases from test-manifests.json pass
- [x] No exceptions thrown, always returns valid response

### Test Commands
```bash
mvn clean test
curl -X POST http://localhost:8080/ -H "Content-Type: application/json" -d '{"url":"https://example.com/video.mp4"}'
curl -X POST http://localhost:8080/ -H "Content-Type: application/json" -d '{"url":""}'
```

---

## Iteration 6: Integration Tests

### Objectives
- Create comprehensive integration test suite
- Validate all requirements end-to-end
- Use test-manifests.json for test data

### Implementation Steps
1. Create `ManifestControllerIntegrationTest` class
2. Load test-manifests.json as test resource
3. Write tests for all valid manifest URLs
4. Write tests for all invalid manifest URLs
5. Verify response structure matches schema
6. Test concurrent requests
7. Generate test coverage report

### Validation Criteria
- [x] All valid test cases return correct streamtype
- [x] All invalid test cases return "invalid"
- [x] Response format matches response.schema.json
- [x] Integration tests run successfully with `mvn verify`
- [x] Test coverage > 80%
- [x] No flaky tests

### Test Commands
```bash
mvn clean verify
mvn test jacoco:report
open target/site/jacoco/index.html
```

---

## Iteration 7: API Documentation

### Objectives
- Add Swagger/OpenAPI integration
- Implement /apidocs endpoint
- Provide comprehensive API documentation

### Implementation Steps
1. Add Springdoc OpenAPI dependency to pom.xml
2. Configure Swagger UI at /apidocs
3. Add OpenAPI annotations to controllers
4. Document request/response schemas
5. Add examples for each endpoint
6. Configure to include health and manifest endpoints
7. Test documentation completeness

### Validation Criteria
- [x] GET /apidocs returns API documentation
- [x] Documentation includes POST / endpoint details
- [x] Documentation includes GET /health endpoint
- [x] Request/response schemas are documented
- [x] Examples are provided for all endpoints
- [x] Documentation is accessible without authentication (AR005)
- [x] Swagger UI is functional and navigable

### Test Commands
```bash
mvn spring-boot:run
open http://localhost:8080/apidocs
curl http://localhost:8080/apidocs
```

---

## Iteration 8: Static Web UI

### Objectives
- Create simple static webpage for testing
- Provide user-friendly interface to call API
- Display results clearly

### Implementation Steps
1. Create `src/main/resources/static/index.html`
2. Add HTML form with URL input field
3. Add JavaScript to handle form submission
4. Make AJAX POST request to / endpoint
5. Display response (streamtype) to user
6. Add basic CSS styling
7. Handle errors gracefully

### Validation Criteria
- [x] Webpage is accessible at http://localhost:8080/
- [x] Form accepts manifest URL input
- [x] Submit button triggers API call
- [x] Response displays streamtype result
- [x] Works with all manifest types (hls, dash, invalid)
- [x] Error messages shown for failed requests
- [x] UI is simple and functional

### Test Commands
```bash
mvn spring-boot:run
open http://localhost:8080/
# Manually test with URLs from test-manifests.json
```

---

## Iteration 9: Docker Containerization

### Objectives
- Create Docker container for application
- Ensure all functionality works in containerized environment
- Meet TR002 requirement

### Implementation Steps
1. Create `Dockerfile` with multi-stage build
2. Use Maven image for build stage
3. Use JRE image for runtime stage
4. Copy JAR and set entry point
5. Expose port 8080
6. Create `.dockerignore` file
7. Build and test container
8. Document Docker commands in README

### Validation Criteria
- [x] `docker build` completes successfully
- [x] Container starts without errors
- [x] All endpoints accessible from host
- [x] Health check returns 200
- [x] API accepts manifest requests
- [x] Static UI loads correctly
- [x] API documentation accessible
- [x] Container size is reasonable (<200MB)

### Test Commands
```bash
docker build -t streamviewservice:latest .
docker run -p 8080:8080 streamviewservice:latest
curl http://localhost:8080/health
curl -X POST http://localhost:8080/ -H "Content-Type: application/json" -d '{"url":"https://example.com/test.m3u8"}'
open http://localhost:8080/
```

---

## Iteration 10: Final Testing & Validation

### Objectives
- Validate all requirements are met
- End-to-end testing in Docker
- Final documentation and cleanup

### Implementation Steps
1. Review all requirements in docs/requirements.md
2. Create checklist mapping implementation to requirements
3. Run full test suite in Docker container
4. Test with all URLs from test-manifests.json
5. Verify performance (response times)
6. Update CLAUDE.md with build/test commands
7. Create README.md with usage instructions
8. Tag release version

### Validation Criteria

#### General Requirements
- [x] GR001: Application name is streamViewService
- [x] GR002: Parses and analyzes both HLS and MPEG-DASH manifests

#### API Requirements
- [x] AR001: POST / endpoint accepts 'url' parameter
- [x] AR002: Manifest URLs with query parameters work
- [x] AR003: Verifies valid manifests, returns "invalid" for invalid ones
- [x] AR004: GET /apidocs returns API documentation
- [x] AR005: No authentication required for any endpoint
- [x] AR006: Returns JSON matching response.schema.json
- [x] AR007: GET /health returns 200 with JSON when healthy
- [x] AR008: Health check returns error code when unhealthy

#### Technical Requirements
- [x] TR001: Built using Java
- [x] TR002: Deployed in single Docker container

#### Additional Features
- [x] Unit tests pass
- [x] Integration tests pass
- [x] Static web UI functional
- [x] All test manifests validated

### Final Test Commands
```bash
# Build and run in Docker
docker build -t streamviewservice:latest .
docker run -d -p 8080:8080 --name svs streamviewservice:latest

# Test all endpoints
curl http://localhost:8080/health
curl http://localhost:8080/apidocs
curl -X POST http://localhost:8080/ -H "Content-Type: application/json" -d '{"url":"https://www.radiantmediaplayer.com/media/rmp-segment/bbb-abr-aes/playlist.m3u8"}'
open http://localhost:8080/

# Cleanup
docker stop svs
docker rm svs
```

---

## Summary

This incremental plan ensures that:
1. Each iteration delivers working, testable functionality
2. Progress can be validated at every step
3. Issues are caught early in small increments
4. The application is continuously in a working state
5. All requirements are systematically addressed
6. Testing is built in from the start

The plan follows best practices for incremental delivery and allows for adjustments between iterations based on learnings and feedback.
