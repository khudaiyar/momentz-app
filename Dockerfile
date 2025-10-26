# Use Java 21 base image
FROM eclipse-temurin:21-jdk-jammy

# Argument for JAR file
ARG JAR_FILE=target/*.jar

# Copy JAR file
COPY ${JAR_FILE} /app.jar

# Set environment variables
ENV PORT=8081

# Expose port
EXPOSE 8081

# Run JAR
ENTRYPOINT ["java","-jar","/app.jar"]
