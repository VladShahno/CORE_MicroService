FROM maven:3.6.3-jdk-11-slim as build
WORKDIR /home/app
COPY src src
COPY pom.xml .
RUN mvn clean package

FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/*.jar /usr/app/CORE.jar
EXPOSE 8082
CMD java -jar -Dspring.data.mongodb.username=$MONGODB_USERNAME -Dspring.data.mongodb.password=$MONGODB_PASSWORD /usr/app/CORE.jar
