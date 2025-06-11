FROM amazoncorretto:21

WORKDIR /app

COPY build/libs/kajikashiApp.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]