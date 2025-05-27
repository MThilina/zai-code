# 🌦️ Zai Weather Service - Java Spring Boot

A RESTful HTTP service that reports the current weather in Melbourne, Australia, including:

- ✅ Temperature in Celsius
- ✅ Wind Speed
- ✅ Fallback to secondary weather provider
- ✅ 3-second cache with stale fallback support
- ✅ Dockerized build and run
- ✅ Circuit breaker for resilience

---

## 🚀 Features

- **Primary Provider**: [WeatherStack](https://weatherstack.com/documentation)
- **Secondary Provider**: [OpenWeatherMap](https://openweathermap.org/current)
- **Caching**: Manual [Caffeine](https://github.com/ben-manes/caffeine) cache (3s TTL)
- **Fallback**: If primary fails, switch to secondary
- **Stale Serve**: Serve cached data if both providers are down
- **Circuit Breaker**: [Resilience4j](https://resilience4j.readme.io/)
- **Built with**: Java 17, Spring Boot 3.x, Maven

---

## 📦 API Spec

### Endpoint

```
GET /v1/weather?city=melbourne
```

### Sample Response

```json
{
  "temperatureDegrees": 26.3,
  "windSpeed": 10.7
}
```

---

## 📁 Project Structure

```
src/
├── main/java/com/zai/weather/
│   ├── controller/          # REST controller
│   ├── service/             # WeatherService with cache/fallback
│   ├── provider/            # WeatherStack & OpenWeather providers
│   ├── dto/                 # Unified & external API DTOs
│   ├── mapper/              # Mapping API DTOs to internal format
│   └── config/              # Bean configs (Caffeine cache, RestTemplate)
└── resources/
    └── application.properties      # (Optional, removed for direct Caffeine use)
```

---

## ⚙️ Build and Run Locally

### 1️⃣ Clone the Repo

```bash
git clone https://github.com/your-username/zai-weather-service.git
cd zai-weather-service
```

### 2️⃣ Build with Maven

```bash
mvn clean install
```

### 3️⃣ Run the App

```bash
mvn spring-boot:run
```

Then open:
```
http://localhost:8080/v1/weather?city=melbourne
```

---

## 🧪 Run Tests

```bash
mvn test
```

---

## 🐳 Docker Instructions

### 1️⃣ Package the Application

```bash
mvn clean package
```

### 2️⃣ Build the Docker Image

```bash
docker build -t zai-weather-app .
```

### 3️⃣ Run the Docker Container

```bash
docker run -p 8080:8080 zai-weather-app
```

### 4️⃣ Hit the API

```bash
curl http://localhost:8080/v1/weather?city=melbourne
```

---

## 🔐 API Keys

Update the following classes with your API keys:

- `WeatherStackProvider.java`
  ```java
  String apiKey = "YOUR_WEATHERSTACK_API_KEY";
  ```
- `OpenWeatherProvider.java`
  ```java
  String apiKey = "YOUR_OPENWEATHERMAP_API_KEY";
  ```

Ensure the keys are active and allowed to call from `localhost` (especially on free tiers).

---

## 📄 Caffeine Cache Behavior

- TTL = 3 seconds
- Cached response is returned for requests within 3 seconds
- If providers fail, stale cache is served (if available)

---

## 🧱 Dependencies

- Java 17
- Spring Boot 3.x
- Spring Web
- Caffeine Cache
- Resilience4j
- Lombok
- Maven

---

## ❓ Troubleshooting

- ❗ **Getting `NullPointerException` for DTO fields?**
  Ensure you're checking for `null` before accessing nested fields and use proper error mapping.

- ❗ **Cache not expiring in 3s?**
  You must **not use `@Cacheable`**. This project uses **manual Caffeine caching** for full control.

- ❗ **Provider APIs fail intermittently?**
  Use a circuit breaker and fallback properly, as shown in `WeatherStackProvider` and `OpenWeatherProvider`.

---

## 📃 License

This project is for assessment and educational purposes only.

---

## 🙋‍♂️ Contact

Author: **Thilina Manawadu**  
Email: `thilinamanawadu@gmail.com`  


