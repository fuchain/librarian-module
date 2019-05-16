FROM node:10.15.3-alpine as builder

RUN apk add yarn

RUN mkdir -p /root/src/app
WORKDIR /root/src/app
ENV PATH /root/src/app/node_modules/.bin:$PATH

COPY . .

RUN yarn

EXPOSE 3000

ENTRYPOINT ["npm","run","serve"]