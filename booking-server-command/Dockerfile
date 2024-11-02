# Build stage
FROM maven:3.8.8-eclipse-temurin-17 as build

# Cài đặt Maven
#RUN apt-get update && apt-get install -y maven

# Thiết lập thư mục làm việc
WORKDIR /app

# Sao chép dependency project
COPY . .

# Build và cài đặt dependency
RUN cd /app/booking-care-document && mvn clean install -DskipTests

# Sao chép main project
COPY . /app/booking-server-command/

# Build ứng dụng chính
RUN cd /app/booking-server-command && mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Sao chép file JAR từ build stage
COPY --from=build /app/booking-server-command/target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]