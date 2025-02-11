FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN chmod +x gradlew
RUN ./gradlew build --no-daemon

CMD ["java", "-jar", "build/libs/YOUR_PROJECT_NAME.jar"]
