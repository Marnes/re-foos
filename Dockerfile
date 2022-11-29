FROM openjdk:11 AS deploy-backend
WORKDIR /app
COPY ./build/libs/*-all.jar /app/epifoos-backend.jar
EXPOSE 8080:8080
ENTRYPOINT ["java","-jar","/app/epifoos-backend.jar"]
