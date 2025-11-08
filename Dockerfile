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

# IMPORTANT: Clean everything first, then build
RUN ./mvnw clean package -DskipTests

# Copy the JAR with explicit name
RUN mv target/momentz-app-1.0.0.jar app.jar

# Expose dynamic port (Render provides $PORT)
EXPOSE 8081

# Start app on dynamic port
CMD ["sh", "-c", "java -Dserver.port=$PORT -jar app.jar"]
