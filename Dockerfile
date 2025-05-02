# Stage 1: Build the application
FROM amazoncorretto:17.0.15-alpine3.21 AS builder

# Set the working directory
WORKDIR /app

# Copy the Maven wrapper files first to leverage Docker cache
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN chmod +x ./mvnw

# Download dependencies (cached if pom.xml/mvnw don't change)
RUN ./mvnw dependency:resolve

# Copy the source code
COPY src ./src

# Package the application, skipping tests
RUN ./mvnw package -DskipTests

# Stage 2: Create the final runtime image
FROM amazoncorretto:17.0.15-alpine3.21

WORKDIR /app

# Copy only the built JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the port the application runs on (default 8080)
EXPOSE 8080

# Set the entrypoint to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
