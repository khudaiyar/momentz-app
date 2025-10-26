FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# Copy project files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Give mvnw execute permission
RUN chmod +x mvnw

# Build the JAR inside Docker
RUN ./mvnw clean package

# Copy the JAR to root
RUN cp target/*.jar /app.jar

EXPOSE 8081
CMD ["java", "-jar", "/app.jar"]
