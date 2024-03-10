FROM openjdk:17-jdk

WORKDIR /app

COPY target/*.jar dao-service.jar

CMD ["java", "-jar", "dao-service.jar"]