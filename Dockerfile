FROM openjdk:17
WORKDIR /app
COPY target/kloun-0.0.1-SNAPSHOT.jar /app/kloun-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "/app/kloun-0.0.1-SNAPSHOT.jar"]