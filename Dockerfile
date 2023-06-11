# Use an OpenJDK base image
FROM openjdk:20-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/wiki-*.jar app.jar

# Expose the port on which your application runs
EXPOSE 8080

# Set the entry point for the container
ENTRYPOINT ["java", "-jar", "app.jar"]