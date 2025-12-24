# ======================
# BUILD STAGE
# ======================
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app

# Copy toàn bộ source 1 lần
COPY . .

# Build module document
WORKDIR /app/booking-care-document
RUN mvn clean install -DskipTests

# Build module command
WORKDIR /app/booking-server-command
RUN mvn clean package -DskipTests


# ======================
# RUNTIME STAGE
# ======================
FROM eclipse-temurin:17-jre
WORKDIR /app

# Set timezone VN
RUN apt-get update && apt-get install -y tzdata && \
    ln -sf /usr/share/zoneinfo/Asia/Ho_Chi_Minh /etc/localtime && \
    echo "Asia/Ho_Chi_Minh" > /etc/timezone && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# Copy jar
COPY --from=build /app/booking-server-command/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
