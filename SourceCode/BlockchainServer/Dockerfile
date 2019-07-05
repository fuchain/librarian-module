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
COPY --from=builder /root/src/app/.env /root/src/app/.env

EXPOSE 5000

ENTRYPOINT ["node","./dist/server.js"]

# docker build -t librarian-blockchainserver .
# docker run -d --name librarian-blockchainserver -p 5000:5000 librarian-blockchainserver:latest