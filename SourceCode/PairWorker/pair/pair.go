package pair

import (
	"context"
	"log"
	"os"
	"time"

	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
)

// Matching ...
type Matching struct {
	ID           primitive.ObjectID `json:"id" bson:"_id,omitempty"`
	Email        string             `json:"email" bson:"email"`
	BookDetailID int                `json:"bookDetailId,omitempty" bson:"bookDetailId,omitempty"`
	BookID       *string            `json:"bookId,omitempty" bson:"bookId,omitempty"`
	Matched      bool               `json:"matched" bson:"matched"`
	Time         int                `json:"time" bson:"time"`
}

// Pair ...
func Pair() {
	log.Println("Starting a pair job...")

	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	var mongoHostname string
	if os.Getenv("MONGODB") != "" {
		mongoHostname = os.Getenv("MONGODB")
	} else {
		mongoHostname = "127.0.0.1:27017"
	}

	client, err := mongo.Connect(ctx, options.Client().ApplyURI("mongodb://"+mongoHostname))
	if err != nil {
		log.Fatal(err)
	}

	collection := client.Database("librarianblockchain").Collection("matchings")
	cur, err := collection.Find(ctx, bson.M{"matched": false})
	if err != nil {
		log.Fatal(err)
	}

	var matchingSlice []Matching

	for cur.Next(ctx) {
		var result Matching
		err := cur.Decode(&result)
		if err != nil {
			log.Fatal(err)
		}

		matchingSlice = append(matchingSlice, result)
	}

	log.Println("Request not matched in queues is: ", len(matchingSlice))

	uniqueBookIDs := distinctSlice(matchingSlice)
	queues := createQueueSlice(uniqueBookIDs, matchingSlice)

	checkQueues(queues)

	if err := cur.Err(); err != nil {
		log.Fatal(err)
	}
}

func distinctSlice(slice []Matching) []int {
	var unique []int
	for _, v := range slice {
		skip := false
		for _, u := range unique {
			if v.BookDetailID == u {
				skip = true
				break
			}
		}

		if !skip {
			unique = append(unique, v.BookDetailID)
		}
	}

	return unique
}

func getMin(a int, b int) int {
	if a > b {
		return b
	}

	return a
}

func createBookDetailIDSlice(slice []Matching, bookDetailID int) []Matching {
	var result []Matching
	for _, v := range slice {
		if v.BookDetailID == bookDetailID {
			result = append(result, v)
		}
	}

	return result
}

func createQueueSlice(bookDetailIds []int, slice []Matching) []([]Matching) {
	var result []([]Matching)
	for _, v := range bookDetailIds {
		queue := createBookDetailIDSlice(slice, v)
		result = append(result, queue)
	}

	return result
}

func createRequestSlice(slice []Matching, isReturning bool) []Matching {
	var result []Matching
	for _, v := range slice {
		if isReturning {
			if v.BookID != nil {
				result = append(result, v)
			}
		} else {
			if v.BookID == nil {
				result = append(result, v)
			}
		}
	}

	return result
}

func checkQueues(queues []([]Matching)) {
	for _, v := range queues {
		returnSlice := createRequestSlice(v, true)
		requestSlice := createRequestSlice(v, false)

		shorterLength := getMin(len(returnSlice), len(requestSlice))

		if shorterLength > 0 {
			for i := 0; i < shorterLength; i++ {
				matchedReturner := returnSlice[i]
				matchedRequester := requestSlice[i]

				HandleMatch(matchedRequester, matchedReturner)
			}
		}
	}
}
