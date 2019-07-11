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
RUN npm run build

FROM node:10.16.0-alpine

RUN apk update
RUN apk add --no-cache \
    build-base bash python linux-headers zlib-dev

WORKDIR /root/src/app
ENV PATH /root/src/app/node_modules/.bin:$PATH

COPY --from=builder /root/src/app/package.json /root/src/app/package.json
COPY --from=builder /root/src/app/package-lock.json /root/src/app/package-lock.json
RUN npm install

COPY --from=builder /root/src/app/dist /root/src/app/dist

EXPOSE 3000

ENTRYPOINT ["node","./dist/server.js"]

# docker build -t librarian-ioserver .
# docker run -d --name librarian-ioserver -p 5002:5002 librarian-ioserver:latest