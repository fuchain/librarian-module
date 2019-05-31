FROM node:10.15.0 as builder

# Create working directory
RUN mkdir -p /root/src/app
WORKDIR /root/src/app
ENV PATH /root/src/app/node_modules/.bin:$PATH

# Copy package.json and yarn.lock
COPY . .

# Fetch dependencies, run script optimize images, build
RUN yarn --pure-lockfile
RUN yarn build

# Copy files to machine
FROM nginx:alpine

WORKDIR /root/src/app

COPY --from=builder /root/src/app/dist/ /usr/share/nginx/html

# Datadog Log Monitoring
LABEL "com.datadoghq.ad.check_names"='["vue"]'
LABEL "com.datadoghq.ad.init_configs"='[{}]'
LABEL "com.datadoghq.ad.logs"='[{"source": "vue", "service": "webapp"}]'

# This is docker build command: 
# docker build -t librarian-webapp .

# This is docker run command:
# docker run -d --name librarian-webapp -p 3000:80 librarian-webapp:latest
