# Base image
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy Gradle files
COPY gradlew /app/
COPY gradle /app/gradle/
COPY build.gradle settings.gradle /app/

## Modify the build.gradle to include mainClass (if not already set)
#RUN sed -i '/springBoot {/a \ \ mainClass = "com.fn.eureka.client.deliveryservice.DeliveryServiceApplication"' /app/build.gradle

# Copy common and delivery-service directories
COPY common /app/common/
COPY delivery-service /app/delivery-service/

# Ensure gradlew is executable
RUN chmod +x /app/gradlew

# Check if gradlew exists
RUN /bin/sh -c "ls -l /app/gradlew"  # 확인용, gradlew가 있는지 확인

# Build the application using gradlew
RUN /app/gradlew build -x test --stacktrace

# Copy the built JAR file to /app.jar
RUN cp delivery-service/build/libs/delivery-service.jar /app.jar

# Expose port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "/app.jar"]
