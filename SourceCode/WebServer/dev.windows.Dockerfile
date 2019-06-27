FROM gradle:5.4.1-jdk12

RUN mkdir -p /root/src/api
WORKDIR /root/src/api

COPY . .

RUN gradle dependencies

ENTRYPOINT ["gradle","bootRun"]