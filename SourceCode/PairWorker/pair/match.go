package pair

import (
	"encoding/json"
	"log"
	"pairworker/caller"

	"go.mongodb.org/mongo-driver/bson/primitive"
)

// Returner ...
type Returner struct {
	ID           primitive.ObjectID `json:"id"`
	Email        string             `json:"email"`
	BookDetailID int                `json:"bookDetailId,omitempty"`
	BookID       string             `json:"bookId,omitempty"`
	Matched      bool               `json:"matched"`
	Time         int                `json:"time"`
}

// Req ...
type Req struct {
	Returner Returner `json:"returner"`
	Receiver Matching `json:"receiver"`
}

// HandleMatch ...
func HandleMatch(returnerRaw Matching, receiver Matching) {
	var bookID string
	if returnerRaw.BookID != nil {
		bookID = *returnerRaw.BookID
	} else {
		bookID = ""
	}

	returner := Returner{
		ID:           returnerRaw.ID,
		Email:        returnerRaw.Email,
		BookDetailID: returnerRaw.BookDetailID,
		BookID:       bookID,
		Matched:      returnerRaw.Matched,
		Time:         returnerRaw.Time,
	}

	values := Req{
		Returner: returner,
		Receiver: receiver,
	}

	requestBody, err := json.Marshal(values)
	if err != nil {
		log.Fatal("Mar erro:", err)
	}

	caller.PostPair("http://localhost:5000/api/v1/remote/pair_update", requestBody)
}
