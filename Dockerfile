FROM openjdk:17-jdk
WORKDIR /workspace
COPY build/libs/*.jar springboot.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","springboot.jar"]