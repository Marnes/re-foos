FROM openjdk:11 AS deploy-backend
WORKDIR /app
COPY ./build/libs/*.jar /app/epifoos-backend.jar

EXPOSE 8080:8080
ENTRYPOINT ["java","-jar","/app/epifoos-backend.jar"]

FROM node:18-alpine AS deploy-frontend
USER node:node
WORKDIR /app
COPY --from=build-node --chown=node:node /app/build ./build
COPY --from=build-node --chown=node:node /app/node_modules ./node_modules
COPY --chown=node:node package.json .

EXPOSE 5050:5050
CMD ["node", "build"]
