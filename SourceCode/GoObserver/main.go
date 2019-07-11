package main

import (
	"encoding/json"
	"fmt"
	"os"
	"time"

	"github.com/huynhminhtufu/observe-worker/transaction"

	"golang.org/x/net/websocket"
)

// BigchainDB Websocket Address and Port
const wsaddress string = "testnet.bigchain.fptu.tech:32796"
const address string = "http://testnet.bigchain.fptu.tech/api/v1"

func main() {
	initWebsocketClient()
}

type Message struct {
	RequestID int
	Command   string
}

type Item struct {
	Height        int    `json:"height"`
	AssetID       string `json:"asset_id"`
	TransactionID string `json:"transaction_id"`
}

func initWebsocketClient() {
	fmt.Println("Starting client...")

	ws, err := websocket.Dial(fmt.Sprintf("ws://%s/api/v1/streams/valid_transactions", wsaddress), "", fmt.Sprintf("http://%s/", wsaddress))
	if err != nil {
		fmt.Printf("Dial failed: %s\n", err.Error())
		os.Exit(1)
	} else {
		fmt.Println("Connected to websocket stream:", wsaddress)
	}

	incomingMessages := make(chan string)
	go readClientMessages(ws, incomingMessages)
	i := 0
	for {
		select {
		case <-time.After(time.Duration(2e9)):
			i++
			response := new(Message)
			response.RequestID = i
			response.Command = "Add messages to channel"
			err = websocket.JSON.Send(ws, response)
			if err != nil {
				fmt.Printf("Send failed: %s\n", err.Error())
				os.Exit(1)
			}
		case message := <-incomingMessages:
			// Unmarshal here
			data := Item{}
			json.Unmarshal([]byte(message), &data)
			fmt.Printf("Height: %d\n", data.Height)
			fmt.Printf("AssetID: %s\n", data.AssetID)
			fmt.Printf("TransactionID: %s\n", data.TransactionID)

			transaction := transaction.GetTransaction(data.TransactionID, address)
			fmt.Printf("Transaction detail is: %v", transaction)
		}
	}
}

func readClientMessages(ws *websocket.Conn, incomingMessages chan string) {
	for {
		var message string
		err := websocket.Message.Receive(ws, &message)
		if err != nil {
			fmt.Printf("Error::: %s\n", err.Error())
			return
		}
		incomingMessages <- message
	}
}
