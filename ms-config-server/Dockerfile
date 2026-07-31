FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY ./pom.xml .
COPY ./src ./src

RUN mvn clean package -DskipTests


FROM eclipse-temurin:21-jre-jammy

RUN addgroup --gid 1000 spring_group && adduser --uid 1000 --disabled-password --gecos "" spring_user --ingroup spring_group
WORKDIR /app
USER spring_user:spring_group

COPY --from=builder /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]