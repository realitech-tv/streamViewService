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
- [ ] AR007: A GET request to the endpoint '/health' should return HTTP 200 with a JSON response body describing the service status when the service is running properly.
- [ ] AR008: The health check endpoint should return an appropriate HTTP error code with a JSON response body describing the service status when the service is not running properly.
- [ ] AR009: When streamtype is "hls", the response may include an optional "hls" object containing HLS-specific stream details.
- [ ] AR010: The "hls" object may contain a "variants" property with an array of variant objects enumerated from 1 onwards, where each variant object shall include the properties "bandwidth" (in bits per second), "codec", "resolution", and "uri" (the child manifest URI for that variant level) populated from the HLS manifest.
- [ ] AR011: When streamtype is "dash", the response may include an optional "dash" object containing DASH-specific stream details.
- [ ] AR012: When the manifest is not a valid HLS or MPEG-DASH manifest (streamtype is "invalid"), the JSON response shall not include the "hls" or "dash" properties.
- [ ] AR013: When the manifest is a valid HLS manifest (streamtype is "hls"), the JSON response shall include the "hls" property and shall not include the "dash" property.
- [ ] AR014: When the manifest is a valid MPEG-DASH manifest (streamtype is "dash"), the JSON response shall include the "dash" property and shall not include the "hls" property.

# UI requirements
- [ ] UR001: A simple webpage should be provided at the endpoint "/ui" which should provide a simple form, allowing the user to send requests to the main API via a web form.
- [ ] UR002: The webpage should be using a simple, clean and modern design.
- [ ] UR003: The webpage should be served from the same web service as the API.
- [ ] UR004: The webpage should have one text field for the manifest URL and a submit button.
- [ ] UR005: When clicking submit, an API request should be sent, and the complete JSON response document presented below the form. The presentation shall include the streamtype and all stream-specific details: for HLS streams, display all variant properties (bandwidth, codec, resolution, uri); for DASH streams, display the dash object contents; for invalid streams, display only the streamtype.

# Technical requirements
- [ ] TR001: The application should be built using Java
- [ ] TR002: The application should be deployed in a single Docker container