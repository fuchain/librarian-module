FROM openjdk:12 AS builder

RUN mkdir -p /root/src/api
WORKDIR /root/src/api

COPY . .

RUN ./gradlew build

FROM openjdk:12-jdk-alpine

WORKDIR /root/src/api

COPY --from=builder /root/src/api/build/libs/LibrarianModule.jar /root/src/api/LibrarianModule.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","LibrarianModule.jar","--spring.profiles.active=prod"]

# This is docker build command: 
# docker build -t librarian-module .

# This is docker run command:
# docker run -d --name librarian-module -p 5000:8080 librarian-module:latest