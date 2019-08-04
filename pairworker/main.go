package main

import (
	"pairworker/server"

	"github.com/getsentry/sentry-go"
)

func main() {
	sentry.Init(sentry.ClientOptions{
		Dsn: "https://0dde0eb533ab441bbb22dd03915688e4@sentry.io/1521090",
	})

	server.StartServer()
}
