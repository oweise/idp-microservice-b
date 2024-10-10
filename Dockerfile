FROM openjdk:17-slim as builder
WORKDIR /workspace/appA
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
RUN ./mvnw install -DskipTests
FROM openjdk:17-slim
COPY --from=builder /workspace/appA/target/microserviceB-*.jar microserviceB.jar
ENTRYPOINT ["java","-jar","/microserviceB.jar"]
