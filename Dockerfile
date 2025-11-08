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

# FORCE REBUILD - Add timestamp to break cache
RUN echo "Build timestamp: $(date)" > build-info.txt

# Clean and build - this will NOT be cached now
RUN ./mvnw clean package -DskipTests

# Copy the JAR
RUN mv target/momentz-app-1.0.0.jar app.jar

# Expose dynamic port
EXPOSE 8081

# Start app
CMD ["sh", "-c", "java -Dserver.port=$PORT -jar app.jar"]