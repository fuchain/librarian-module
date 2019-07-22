FROM golang:alpine
RUN apk add ca-certificates git

RUN mkdir -p /root/src/go
WORKDIR /root/src/go
COPY go.mod go.sum ./
RUN go mod download

COPY . .

EXPOSE 5100

ENTRYPOINT ["go","run","main.go"]

# This is docker build command: 
# docker build -t pair-worker .

# This is docker run command:
# docker run -it -v $(pwd):/root/src/go -p 5100:5100 pair-worker:latest