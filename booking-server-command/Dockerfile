# Use a base image with Java pre-installed
FROM openjdk:11

# Set the working directory in the container
WORKDIR /src/java/com/example/bookingserver
#skip task: test
RUN gradle clean build -x test --no-daemon
# Copy the Java application JAR file into the container
COPY target/booking-server-command-0.0.1-SNAPSHOT.jar .

# Command to run your Java application
CMD ["java", "-jar", "booking-server-command-0.0.1-SNAPSHOT.jar"]