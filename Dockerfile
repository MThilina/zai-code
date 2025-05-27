# Use lightweight JDK 17 base image
FROM eclipse-temurin:17-jdk-alpine

# Set build-time argument for the jar file
ARG JAR_FILE=target/*.jar

# Copy the jar file into the container
COPY ${JAR_FILE} app.jar

# Expose the application port
EXPOSE 8081

# Run the application
ENTRYPOINT ["java","-jar","/app.jar"]
