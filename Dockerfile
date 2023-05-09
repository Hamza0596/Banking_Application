FROM openjdk:8
EXPOSE 8080
ADD target/BankingApplication-0.0.1-SNAPSHOT.jar BankingApplication.jar
ENTRYPOINT ["java", "-jar","/BankingApplication.jar"]