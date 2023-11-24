# build executable .jar file
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests
# run buided .jar
FROM openjdk:17-oracle
COPY --from=build /target/petmarket.jar petmarket.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "petmarket.jar"]