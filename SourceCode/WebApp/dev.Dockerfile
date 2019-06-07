FROM node:10.16.0 as builder

RUN mkdir -p /root/src/app
WORKDIR /root/src/app
ENV PATH /root/src/app/node_modules/.bin:$PATH

COPY . .

RUN yarn

EXPOSE 3000

ENTRYPOINT ["npm","run","serve"]