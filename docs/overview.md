The **streamViewService** application is provided as a RESTful web service that finds information about video streams based on manifest URLs. It provides a simple API where requests are sent with a manifest URL as a POST parameter. The response is a JSON structure with the information about the video.

Some of the key features:
- The service is provided in a single Docker container which contains all the required functionality for the API. 
- Automated testing will be provided as standard, both on the unit level and on the integration test level.
- The API will have built-in API docs that can be accessed over the API itself.
- A simple static webpage will be provided, where requests can be sent to the API from a UI.

The application will be built incrementally, using the Planning Mode of Claude Code.


