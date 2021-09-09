FROM openjdk:8u292-jdk-slim
ADD target/uav-controler-server-1.0.jar application.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Duser.timezone=GMT+0","-Dspring.profiles.active=dev","-jar","/application.jar"]
