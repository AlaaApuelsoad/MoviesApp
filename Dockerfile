# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/MoviesApp-0.0.1-SNAPSHOT.jar MoviesApp-0.0.1-SNAPSHOT.jar

# Expose the port your app runs on
EXPOSE 5055

# Command to run the application
ENTRYPOINT ["java", "-jar", "MoviesApp-0.0.1-SNAPSHOT.jar"]