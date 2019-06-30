FROM openjdk:12 AS builder

RUN mkdir -p /root/src/api
WORKDIR /root/src/api

COPY gradle ./gradle
COPY build.gradle gradlew settings.gradle ./
RUN ./gradlew dependencies

COPY . .
RUN ./gradlew build

FROM openjdk:12-jdk-alpine

WORKDIR /root/src/api

COPY --from=builder /root/src/api/build/libs/LibrarianModule.jar /root/src/api/LibrarianModule.jar

EXPOSE 8080

LABEL "com.datadoghq.ad.check_names"='["java"]'
LABEL "com.datadoghq.ad.init_configs"='[{}]'
LABEL "com.datadoghq.ad.logs"='[{"source": "java", "service": "webserver"}]'

ENTRYPOINT ["java","-jar","LibrarianModule.jar"]

# docker build -t librarian-webserver .
# docker run -d --name librarian-webserver --network=host -p 5000:8080 librarian-webserver:latest
