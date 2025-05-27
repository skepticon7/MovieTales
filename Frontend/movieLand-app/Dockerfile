FROM node:18 AS build-stage

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .

RUN npm run build --prod

FROM node:18-alpine AS production-stage

WORKDIR /app

# Install http-server globally
RUN npm install -g http-server

COPY --from=build-stage /app/dist/movie-land-app/browser ./dist

EXPOSE 80

CMD ["http-server", "./dist", "-p", "80"]
