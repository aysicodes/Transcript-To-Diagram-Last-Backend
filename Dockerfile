FROM openjdk:17-jdk-slim AS build

WORKDIR /app

COPY pom.xml .

# Используем Maven Wrapper для Linux
RUN chmod +x mvnw && ./mvnw dependency:go-offline

COPY . .

# Сборка проекта
RUN ./mvnw clean install -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/TranscriptToDiagram-1.0-SNAPSHOT.jar"]
