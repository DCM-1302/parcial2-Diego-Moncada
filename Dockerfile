FROM gradle:7.6-jdk17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy only the essential files first to benefit from Docker's cache
COPY build.gradle settings.gradle /app/

# Download dependencies (this layer will be cached)
RUN gradle build -x test --no-daemon

# Copy the rest of the project files
COPY . /app/

# Build the project
RUN gradle build -x test --no-daemon

# Use a minimal base image for running the Spring Boot app
FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/build/libs/*.jar /app/app.jar

# Expose the port that Spring Boot will run on
EXPOSE 8080

# Set environment variables for connecting to MySQL
ENV SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/yms
ENV SPRING_DATASOURCE_USERNAME=yms_user
ENV SPRING_DATASOURCE_PASSWORD=yms_clave

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/app.jar"
