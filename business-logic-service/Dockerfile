FROM openjdk:17-jdk

WORKDIR /app

COPY target/*.jar payment-business-logic-service.jar

CMD ["java", "-jar", "payment-business-logic-service.jar"]