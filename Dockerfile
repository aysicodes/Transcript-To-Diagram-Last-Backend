# 1. Используем минимальный образ OpenJDK 17
FROM openjdk:17-jdk-slim AS build

# 2. Устанавливаем рабочую директорию
WORKDIR /app

# 3. Копируем файл pom.xml и скрипты Maven Wrapper
COPY pom.xml mvnw mvnw.cmd ./

# 4. Даем права на выполнение mvnw
RUN chmod +x mvnw

# 5. Скачиваем зависимости, чтобы ускорить сборку
RUN ./mvnw dependency:go-offline

# 6. Копируем оставшиеся файлы проекта
COPY . .

# 7. Собираем приложение (без тестов для ускорения)
RUN ./mvnw clean package -DskipTests

# 8. Создаем финальный контейнер на основе OpenJDK 17
FROM openjdk:17-jdk-slim

# 9. Устанавливаем рабочую директорию
WORKDIR /app

# 10. Копируем собранный JAR-файл из предыдущего этапа
COPY --from=build /app/target/TranscriptToDiagram-1.0-SNAPSHOT.jar app.jar

# 11. Открываем порт 8080
EXPOSE 8080

# 12. Запускаем приложение
CMD ["java", "-jar", "app.jar"]
