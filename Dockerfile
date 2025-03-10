# Use OpenJDK as base image
FROM openjdk:17
WORKDIR /app

# Copy the built application JAR
COPY build/libs/nba-stats-*.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
