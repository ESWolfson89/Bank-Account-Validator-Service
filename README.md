# Bank-Account-Validator-Service
---------

Spring boot rest services that polls sources and validates a bank account number

This app was written on Windows

---------

There are 2 Java files in:
BankAccountValidatorService/src/main/java/com/example/BankAccountValidatorService/
1) BankAccountValidatorService.java
2) MockSources.java

The application.properties file is in:
BankAccountValidatorService/src/main/resources

---------

External Requirements:
1) Eclipse
2) LibericaJDK-11 JRE
3) Curl

---------

To run go into the project directory and type:
mvnw spring-boot:run

![Alt text](/BankAccountValidatorService/springbootrunning.png?raw=true "Screenshot")

---------

The account numbers for a source that are defined as valid goes by the range

[12345678-12345682] + (sourceNum-1)*5

where sourceNum is the position of the source as listed in application.properties

For example if sources are defined as "sources.names=source1,source2":

[12345678-12345682] are valid for source1

[12345683-12345687] are valid for source2

---------

The following tests were done using the curl command in Windows:

1) For application.properties file:

spring.application.name=BankAccountValidatorService

sources.names=source1,source2,source3

sources.urls=http\://localhost:8080/source1,http\://localhost:8080/source2,http\://localhost:8080/source3



Terminal output:
![Alt text](/BankAccountValidatorService/tests1.png?raw=true "Screenshot")


2) For application.properties file:

spring.application.name=BankAccountValidatorService

sources.names=source1,source2,source3,source4

sources.urls=http\://localhost:8080/source1,http\://localhost:8080/source2,http\://localhost:8080/source3,http\://localhost:8080/source4



Terminal output:
![Alt text](/BankAccountValidatorService/tests2.png?raw=true "Screenshot")
