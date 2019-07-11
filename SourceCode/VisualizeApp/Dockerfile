FROM node:10.16.0 as builder

RUN mkdir -p /root/src/app
WORKDIR /root/src/app
ENV PATH /root/src/app/node_modules/.bin:$PATH

COPY package.json package-lock.json ./
RUN npm install

COPY . .
RUN npm run build

FROM nginx:alpine

WORKDIR /root/src/app

COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=builder /root/src/app/build /var/www/html

LABEL "com.datadoghq.ad.check_names"='["react"]'
LABEL "com.datadoghq.ad.init_configs"='[{}]'
LABEL "com.datadoghq.ad.logs"='[{"source": "react", "service": "webapp"}]'

# docker build -t librarian-visualizeapp .
# docker run -d --name librarian-visualizeapp -p 3000:80 librarian-visualizeapp:latest