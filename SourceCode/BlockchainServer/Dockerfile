FROM node:10.16.0-alpine as builder

RUN apk update
RUN apk add --no-cache \
    build-base bash python linux-headers zlib-dev
RUN apk add yarn

RUN mkdir -p /root/src/app
WORKDIR /root/src/app
ENV PATH /root/src/app/node_modules/.bin:$PATH

COPY package.json yarn.lock ./
RUN yarn --pure-lockfile

COPY . .
RUN yarn build

FROM node:10.16.0-alpine

RUN apk update
RUN apk add --no-cache \
    build-base bash python linux-headers zlib-dev
RUN apk add yarn

WORKDIR /root/src/app
ENV PATH /root/src/app/node_modules/.bin:$PATH

COPY --from=builder /root/src/app/package.json /root/src/app/package.json
COPY --from=builder /root/src/app/yarn.lock /root/src/app/yarn.lock
RUN yarn --production --pure-lockfile

COPY --from=builder /root/src/app/dist /root/src/app/dist
COPY --from=builder /root/src/app/.env /root/src/app/.env

EXPOSE 3000

ENTRYPOINT ["node","./dist/server.js"]

# docker build -t librarian-blockchainserver .
# docker run -d --name librarian-blockchainserver -p 5000:5000 librarian-blockchainserver:latest