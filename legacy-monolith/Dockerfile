FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY ./pom.xml .
COPY ./src ./src

RUN mvn clean package -DskipTests


FROM eclipse-temurin:21-jre-jammy

RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring

COPY --from=builder /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]