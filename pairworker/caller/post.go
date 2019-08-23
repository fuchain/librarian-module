package caller

import (
	"bytes"
	"io/ioutil"
	"log"
	"net/http"
)

// PostPair ...
func PostPair(url string, requestBody []byte) {
	resp, err := http.Post(url, "application/json", bytes.NewBuffer(requestBody))
	if err != nil {
		log.Fatal((err))
	}

	defer resp.Body.Close()

	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		log.Fatal(err)
	}

	log.Println("Calling API: ", url)
	log.Println("Response from API: ", string(body))
}
