FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /home/moviesapp
COPY ./pom.xml /home/moviesapp/pom.xml
COPY ./src/main/java/com/alaa/moviesapp/MoviesAppApplication.java /home/moviesapp/src/main/java/com/alaa/moviesapp/MoviesAppApplication.java
RUN mvn -f /home/moviesapp/pom.xml clean package

COPY . /home/moviesapp
RUN mvn -f /home/moviesapp/pom.xml clean package

FROM eclipse-temurin:21-jre
RUN groupadd -r spring && useradd -r -g spring spring
WORKDIR /app
COPY target/app.jar app.jar
RUN chown -R spring:spring /app
USER spring
ENTRYPOINT ["java", "-jar", "app.jar"]