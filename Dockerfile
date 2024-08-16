FROM openjdk:21-jdk
COPY target/crypto-investment-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]