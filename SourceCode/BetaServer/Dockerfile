FROM node:10.16.0-alpine as builder

RUN apk update
RUN apk add --no-cache \
    build-base bash python linux-headers zlib-dev
RUN apk add yarn

RUN mkdir -p /root/src/app
WORKDIR /root/src/app
ENV PATH /root/src/app/node_modules/.bin:$PATH

# Copy package.json and yarn.lock
COPY package.json yarn.lock ./
# Fetch dependencies
RUN yarn --pure-lockfile

# Copy source code and build
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

EXPOSE 3000

ENTRYPOINT ["node","./dist/server.js"]

# This is docker build command: 
# docker build -t beta-server .

# This is docker run command:
# docker run -d --name beta-server -p 5000:3000 beta-server:latest