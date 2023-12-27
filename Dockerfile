FROM openjdk:17-jdk
WORKDIR /workspace
COPY build/libs/*.jar springboot.jar
EXPOSE 8082
CMD ["java", "-jar", "springboot.jar"]