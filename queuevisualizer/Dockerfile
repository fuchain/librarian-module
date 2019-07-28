FROM node:10.16.0-alpine as builder

RUN apk update
RUN apk add --no-cache \
    build-base bash python linux-headers zlib-dev

RUN mkdir -p /root/src/app
WORKDIR /root/src/app
ENV PATH /root/src/app/node_modules/.bin:$PATH

COPY package.json package-lock.json ./
RUN npm install

COPY . .

EXPOSE 5000

ENTRYPOINT ["node","./index.js"]

# docker build -t librarian-queuevisualize .
# docker run -d --name librarian-queuevisualize -p 4567:4567 librarian-queuevisualize:latest