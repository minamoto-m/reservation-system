FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

RUN ./mvnw dependency:go-offline

COPY src src

RUN ./mvnw package -DskipTests
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "target/reservation-system-0.0.1-SNAPSHOT.jar"]