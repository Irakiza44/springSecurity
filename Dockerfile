FROM openjdk:17
EXPOSE 8080
COPY target/Spring-Security-docker.jar .
ENTRYPOINT ["java","-jar","/Spring-Security-docker.jar"]