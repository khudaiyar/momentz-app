# Use Java 21 JDK
FROM eclipse-temurin:21-jdk-jammy

# Set working directory
WORKDIR /app

# Copy Maven wrapper and project files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Give execute permission to Maven wrapper
RUN chmod +x mvnw

# Build the project and skip tests for faster build
RUN ./mvnw clean package -DskipTests

# Copy the built JAR to the container root
RUN cp target/*.jar app.jar

# Expose dynamic port (Render provides $PORT)
EXPOSE 8081

# Start the app using the dynamic port
CMD ["sh", "-c", "java -Dserver.port=$PORT -jar app.jar"]

ENV DATABASE_URL=jdbc:postgresql://db.tuburlejceosztccijdk.supabase.co:5432/postgres
ENV DB_USERNAME=postgres
ENV DB_PASSWORD=your_password
