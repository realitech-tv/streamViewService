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

## Implementation Considerations

When building this service, keep in mind:

- The service will parse video manifest files (likely HLS/DASH formats)
- Response format is JSON
- The web UI should be simple and static (no complex frontend framework needed)
- All components must work within a containerized environment
