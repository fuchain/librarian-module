package server

import (
	"encoding/json"
	"log"
	"net/http"
	"pairworker/pair"
)

// Response ...
type Response struct {
	Message string `json:"message"`
}

// StartServer ...
func StartServer() {
	log.Println("Starting pair worker on http://localhost:5100")

	http.HandleFunc("/", requestHandler)
	if err := http.ListenAndServe(":5100", nil); err != nil {
		panic(err)
	}
}

func requestHandler(w http.ResponseWriter, r *http.Request) {
	data := Response{Message: "Executed"}
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(data)

	// Run queue
	pair.Pair()
}
