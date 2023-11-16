FROM maven:3.8.1-openjdk-17-slim
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests
EXPOSE $PORT
CMD ["sh", "-c", "java -jar -Dserver.port=${PORT:-8080} -Dspring.profiles.active=prd target/relationship-hub-api.jar"]