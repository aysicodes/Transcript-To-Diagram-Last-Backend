# Используем официальный образ OpenJDK для сборки и запуска Spring Boot приложения
FROM openjdk:17-jdk-slim AS build

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем pom.xml (если используете Maven) или build.gradle (если используете Gradle)
COPY pom.xml .

# Загружаем зависимости (для Maven)
RUN ./mvnw dependency:go-offline

# Копируем весь проект
COPY . .

# Собираем приложение с помощью Maven
RUN ./mvnw clean install -DskipTests

# Открываем порт 8080 для взаимодействия с приложением
EXPOSE 8080

# Запускаем Spring Boot приложение
CMD ["java", "-jar", "target/TranscriptToDiagram-1.0-SNAPSHOT.jar"]
