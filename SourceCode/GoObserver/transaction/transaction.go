package transaction

import (
	"encoding/json"

	"github.com/huynhminhtufu/observe-worker/common"
)

type Transaction struct {
	Inputs    []InputItem  `json:"inputs"`
	Outputs   []OutputItem `json:"outputs"`
	Operation string       `json:"operation"`
	Metadata  string       `json:"metadata"`
	Asset     Asset        `json:"asset"`
	Version   string       `json:"version"`
	ID        string       `json:"id"`
}

type InputItem struct {
	OwnersBefore []string `json:"owners_before"`
	FullFills    string   `json:"fullfills"`
	FullFillment string   `json:"fullfillment"`
}

type OutputItem struct {
	PublicKeys []string  `json:"public_keys"`
	Condition  Condition `json:"condition"`
	Amount     string    `json:"amount"`
}

type Condition struct {
	Details ConditionDetail `json:"details"`
	URI     string          `json:"uri"`
}

type ConditionDetail struct {
	Type      string `json:"type"`
	PublicKey string `json:"public_key"`
}

type Asset struct {
	Data AssetData `json:"data"`
}

type AssetData struct {
	Type string `json:"type"`
}

func GetTransaction(id string, address string) Transaction {
	body := common.CallHTTP(address + "/transactions/" + id)

	data := Transaction{}
	json.Unmarshal([]byte(body), &data)

	return data
}
