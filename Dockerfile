FROM node:8.12-alpine as npm
RUN mkdir -p /usr/src/app
COPY . /usr/src/app
WORKDIR /usr/src/app
RUN npm install

FROM clojure:lein-2.8.1-alpine as clojure
RUN mkdir -p /usr/src/app
COPY --from=npm /usr/src/app /usr/src/app
WORKDIR /usr/src/app
RUN lein cljsbuild once prod
RUN lein cljsbuild once server-prod

FROM node:8.12-alpine
RUN mkdir -p /usr/src/app
COPY --from=clojure /usr/src/app /usr/src/app
WORKDIR /usr/src/app
RUN npm run workbox
CMD ["node", "target/server/prod/js/compiled/server.js", "3000"]
