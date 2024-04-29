# Stage 1: Build stage
FROM maven:3.8.4-openjdk-17-slim AS build

# Copy Maven files for dependency resolution
COPY ./findFounder/pom.xml ./
COPY ./findFounder/.mvn .mvn
COPY ~.env .env

# Copy application source code
COPY ./findFounder/src src

# Build the project and create the executable JAR
RUN mvn package

# Stage 2: Run stage
FROM openjdk:17-jdk-slim

# Set the working directory for the application
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /target/*.jar app.jar

# Command to run the application
CMD sh -c "java -jar app.jar"
