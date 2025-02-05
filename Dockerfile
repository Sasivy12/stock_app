FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/stock_app.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]