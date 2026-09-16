FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /home/moviesapp
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
RUN groupadd -r spring && useradd -r -g spring spring
WORKDIR /app
COPY --from=build /home/moviesapp/target/*.jar app.jar
RUN chown -R spring:spring /app
USER spring
ENTRYPOINT ["java", "-jar", "app.jar"]