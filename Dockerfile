# Используем образ с OpenJDK
FROM openjdk:17-jdk-slim AS build

# Устанавливаем Maven
RUN apt-get update && apt-get install -y maven

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файл pom.xml
COPY pom.xml .

# Загружаем зависимости
RUN mvn dependency:go-offline

# Копируем весь проект
COPY . .

# Сборка проекта
RUN mvn clean install -DskipTests

# Открываем порт
EXPOSE 8080

# Команда для запуска приложения
CMD ["java", "-jar", "target/TranscriptToDiagram-1.0-SNAPSHOT.jar"]
