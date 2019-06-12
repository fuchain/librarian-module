FROM node:10.16.0 as builder

RUN mkdir -p /root/src/app
WORKDIR /root/src/app
ENV PATH /root/src/app/node_modules/.bin:$PATH

COPY package.json yarn.lock ./
RUN yarn --pure-lockfile

COPY . .
RUN yarn build

FROM nginx:alpine

WORKDIR /root/src/app

COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=builder /root/src/app/dist /var/www/html

LABEL "com.datadoghq.ad.check_names"='["vue"]'
LABEL "com.datadoghq.ad.init_configs"='[{}]'
LABEL "com.datadoghq.ad.logs"='[{"source": "vue", "service": "webapp"}]'

# docker build -t librarian-webapp .
# docker run -d --name librarian-webapp -p 3000:80 librarian-webapp:latest
