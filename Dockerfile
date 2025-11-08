# Use Java 21 JDK
FROM eclipse-temurin:21-jdk-jammy

# Working directory
WORKDIR /app

# Copy Maven wrapper & project files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Make Maven wrapper executable
RUN chmod +x mvnw

# IMPORTANT: Force cache break with timestamp
RUN date > /tmp/build-timestamp.txt && cat /tmp/build-timestamp.txt

# Clean and build application
RUN ./mvnw clean package -DskipTests

# Move JAR file
RUN mv target/momentz-app-1.0.0.jar app.jar

# Expose port
EXPOSE 8081

# Run application
CMD ["sh", "-c", "java -Dserver.port=$PORT -jar app.jar"]