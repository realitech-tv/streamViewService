# General requirements
- [ ] GR001: The application name shall be streamViewService.
- [ ] GR002: The application should be able to parse and analyse both HLS and MPEG-DASH manifests.

# API requirements
- [ ] AR001: The main service request should be available on the root path as a POST request with the following body parameters:
  - [ ] 'url': the URL manifest for the video stream.
- [ ] AR002: The should be possible for the manifest URL supplied to the service to have query parameters.
- [ ] AR003: The service should be able to verify that the URL references a valid HLS or MPEG-DASH manifest. When the URL does not reference a valid manifest, the response should have streamtype set to "invalid".
- [ ] AR004: A GET request to the endpoint '/apidocs' without any parameters should return documentation about the services available through the API.
- [ ] AR005: No authentication or authorization shall be needed to call any of the services.
- [ ] AR006: The service should return a JSON document following the schema "src/main/resources/schemas/response.schema.json".

# Technical requirements
- [ ] TR001: The application should be built using Java
- [ ] TR002: The application should be deployed in a single Docker container