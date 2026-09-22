FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -Dspring.datasource.url=$SPRING_DATASOURCE_URL -Dspring.datasource.username=$SPRING_DATASOURCE_USERNAME -Dspring.datasource.password=$SPRING_DATASOURCE_PASSWORD -jar app.jar"]