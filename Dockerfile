# Build stage
FROM maven:3-openjdk-18 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Package stage
FROM openjdk:18-alpine
WORKDIR /app
COPY --from=build /app/target/notification-service.jar notification-service.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","notification-service.jar"]