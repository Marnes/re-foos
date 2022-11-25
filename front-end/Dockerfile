FROM node:18-alpine AS build-node

WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci
COPY . .
RUN npm run build && npm prune --production

FROM node:18-alpine AS deploy-node
USER node:node
WORKDIR /app
COPY --from=build-node --chown=node:node /app/build ./build
COPY --from=build-node --chown=node:node /app/node_modules ./node_modules
COPY --chown=node:node package.json .

ENV PORT 5050
ENV VITE_ENVIRONMENT "dev"
ENV VITE_HOST="http://localhost:8080"

EXPOSE 5050
CMD ["node", "build"]
