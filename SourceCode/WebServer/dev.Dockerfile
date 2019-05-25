FROM openjdk:12

RUN mkdir -p /root/src/api
WORKDIR /root/src/api

COPY . .

RUN ./gradlew build --refresh-dependencies

ENTRYPOINT ["./gradlew","bootRun"]