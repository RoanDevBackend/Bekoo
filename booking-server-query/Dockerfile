# Stage 1: Build the application using Maven
FROM maven:3.8.8-eclipse-temurin-17 AS build
# Set working directory inside the container
WORKDIR /app
# Copy the Maven project file and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -X
# Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create a lightweight final image with just the JAR
FROM eclipse-temurin:17-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/booking-server-query-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the application port (change if necessary)
EXPOSE 8080

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
