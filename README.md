# Bank-Account-Validator-Service
Spring boot rest services that polls sources and validates a bank account number

There are 2 Java files in:
BankAccountValidatorService/src/main/java/com/example/BankAccountValidatorService/
1) BankAccountValidatorService.java
2) MockSources.java

The application.properties file is in:
BankAccountValidatorService/src/main/resources

External Requirements:
1) Eclipse
2) LibericaJDK-11 JRE
3) Curl

To run go into the project directory and type:
mvnw spring-boot:run

The following tests were done using the curl command in Windows:
1) For application.properties file
spring.application.name=BankAccountValidatorService
sources.names=source1,source2,source3
sources.urls=http\://localhost:8080/source1,http\://localhost:8080/source2,http\://localhost:8080/source3

2) For application.properties file
spring.application.name=BankAccountValidatorService
sources.names=source1,source2,source3,source4
sources.urls=http\://localhost:8080/source1,http\://localhost:8080/source2,http\://localhost:8080/source3,http://localhost:8080/source4
