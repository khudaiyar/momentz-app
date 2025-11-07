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

# Build the project without tests
RUN ./mvnw clean package -DskipTests

# Copy the JAR to container root
RUN cp target/*.jar app.jar

# Expose dynamic port (Render provides $PORT)
EXPOSE 8081

# Start app on dynamic port
CMD ["sh", "-c", "java -Dserver.port=$PORT -jar app.jar"]
