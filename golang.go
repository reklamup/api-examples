package main

import (
	"crypto/hmac"
	"crypto/sha512"
	"fmt"
	"github.com/go-resty/resty/v2"
	"github.com/pkg/errors"
	"strconv"
	"time"
)

func main() {
	apiKey := ""                                       // apiKey := "paste key here"
	apiSecret := ""                                    // apiSecret := "paste secret here"
	nonce := strconv.Itoa(int(time.Now().UnixMicro())) // timestamp millisecond

	requestUrl := "/external/v3/app-report?startDate=2022-10-08&endDate=2022-10-09"
	requestMethod := "GET"

	signaturePayload := fmt.Sprintf("%s.%s.%s.%s", apiKey, nonce, requestUrl, requestMethod)

	hashFunc := hmac.New(sha512.New384, []byte(apiSecret))
	hashFunc.Write([]byte(signaturePayload))

	// The authentication signature is hashed using the apiSecret
	signature := fmt.Sprintf("%x", hashFunc.Sum(nil))

	reportRes := map[string]any{}
	errRes := map[string]any{}

	res, err := resty.New().
		R().
		SetHeader("Content-Type", "application/json").
		SetHeader("x-rup-api-key", apiKey).
		SetHeader("x-rup-api-nonce", nonce).
		SetHeader("x-rup-api-signature", signature).
		SetResult(&reportRes).
		SetError(&errRes).
		Get(fmt.Sprintf("https://api.reklamup.com%s", requestUrl))

	if err != nil {
		fmt.Println(errors.Wrap(err, "client error"))
		return
	}

	if res.StatusCode() != 200 {
		fmt.Println(errors.Errorf("request error statusCode=%d, res=%v", res.StatusCode(), errRes))
		return
	}

	fmt.Println(reportRes)
}
