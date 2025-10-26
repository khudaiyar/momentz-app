# Use Java 21 JDK base image
FROM eclipse-temurin:21-jdk-jammy

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Copy source code
COPY src src

# Build the JAR
RUN ./mvnw clean package

# Copy the JAR to root
RUN cp target/*.jar /app.jar

# Expose port
EXPOSE 8081

# Run the JAR
CMD ["java", "-jar", "/app.jar"]
