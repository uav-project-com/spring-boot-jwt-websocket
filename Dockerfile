FROM openjdk:14.0.2-jdk-slim-buster
ADD target/uav-controler-server-1.0.jar application.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Duser.timezone=GMT+7","-Dspring.profiles.active=dev","-jar","/application.jar"]
#CMD tail -f /dev/null
