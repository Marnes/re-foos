FROM node:18-alpine AS deploy-frontend
WORKDIR /app
COPY ./front-end/build ./build
COPY ./front-end/node_modules ./node_modules
COPY ./front-end/package.json .
ENV PORT 5050
EXPOSE 5050:5050
CMD ["node", "build"]
