# Use official JDK image as base
FROM eclipse-temurin:17-jdk

# Set working directory inside container
WORKDIR /app

# Copy the built jar
COPY target/*.jar app.jar

# Expose Spring Boot port
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
