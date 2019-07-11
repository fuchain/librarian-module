FROM golang:alpine as builder

COPY . $GOPATH/src/github.com/huynhminhtufu/observe-worker/
WORKDIR $GOPATH/src/github.com/huynhminhtufu/observe-worker/

ENTRYPOINT ["go","run","main.go"]

# This is docker build command: 
# docker build -t observe-worker .

# This is docker run command:
# docker run -it -v $(pwd):/go/src/github.com/huynhminhtufu/observe-worker observe-worker:latest