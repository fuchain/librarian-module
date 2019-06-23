FROM node:10.16.0 as builder

RUN mkdir -p /root/src/node
WORKDIR /root/src/node
ENV PATH /root/src/node/node_modules/.bin:$PATH

COPY . .

RUN yarn

EXPOSE 5002

ENTRYPOINT ["yarn","serve"]