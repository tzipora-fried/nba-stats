FROM openjdk:17
WORKDIR /app
COPY build/libs/NBAStats-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
