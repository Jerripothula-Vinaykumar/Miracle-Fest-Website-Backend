# Use official lightweight Java image
FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy the packaged jar file to the container
COPY target/*.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Start the app
ENTRYPOINT ["java", "-jar", "app.jar"]
