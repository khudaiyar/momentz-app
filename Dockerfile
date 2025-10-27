# Use Java 21
FROM eclipse-temurin:21-jdk-jammy

# Set working directory
WORKDIR /app

# Copy Maven wrapper and project files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Give execute permission to mvnw (important!)
RUN chmod +x mvnw

# Build the project
RUN ./mvnw clean package -DskipTests

# Copy the built JAR file to the container
RUN cp target/*.jar app.jar

# Expose Render port
EXPOSE 8081

# Run the app
CMD ["java", "-jar", "app.jar"]
