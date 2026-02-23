# Giai đoạn Build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml trước để tải thư viện (tận dụng cache Docker)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy toàn bộ code src vào để build
COPY src ./src
RUN mvn clean package -DskipTests

# Giai đoạn Run
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Lấy file .jar đã build từ bước trước
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]