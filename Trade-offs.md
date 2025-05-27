## ⚖️ Trade-offs and Future Improvements

### ✅ Trade-offs Made:

1. **Hardcoded API Keys**  
   For simplicity and to meet the time constraint, API keys were directly embedded in the code.  
   🔁 _Improvement_: Externalize them into environment variables or a secure config server.

2. **City Hardcoded to Melbourne**  
   Although the controller accepts a `city` query param, the API calls were restricted to Melbourne per the task description.  
   🔁 _Improvement_: Support multiple cities with validation, normalization, and user input sanitization.

3. **No Schema Validation or Error Response Parsing**  
   The project assumes provider responses are in correct format. No `success: false` or error codes from providers are handled.  
   🔁 _Improvement_: Add structured error parsing and map provider errors into internal error DTOs for better observability.

4. **Minimal Logging and Monitoring**  
   Logging is included, but no distributed tracing, metrics, or alerting mechanisms were set up.  
   🔁 _Improvement_: Integrate with Spring Boot Actuator, Prometheus, and Zipkin for full observability.

5. **Manual Cache Handling over Spring Cache Abstraction**  
   Manual use of Caffeine cache gives fine-grained control over stale fallback, but loses flexibility for switching to Redis or other providers.  
   🔁 _Improvement_: Abstract cache logic to allow easy switching to Redis, Hazelcast, etc.

6. **Circuit Breaker is Applied, But Not Tuned**  
   Default circuit breaker configurations were used.  
   🔁 _Improvement_: Tune failure thresholds, fallback timeout, and sliding window size based on load testing.

7. **No Rate Limiting or Security Layer**  
   The API is open without throttling or security for internal misuse.  
   🔁 _Improvement_: Add rate-limiting with Spring Cloud Gateway and secure endpoints via API Key or OAuth2.

---

### 🧠 If More Time Was Available:

- Add **Swagger/OpenAPI documentation**
- Use **Testcontainers or WireMock** to fully mock third-party APIs in integration tests
- Deploy the app using **Docker Compose** or **Kubernetes**
- Set up **CI/CD pipeline to AWS/GCP** using GitHub Actions
- Enable **i18n (internationalization)** for units and city support globally