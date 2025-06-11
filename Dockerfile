FROM gradle:8.5-jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle bootJar --no-daemon
FROM amazoncorretto:21
WORKDIR /app
COPY --from=builder /app/build/libs/kajikashiApp.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
