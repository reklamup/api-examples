#!/bin/bash

API_KEY="" #API_KEY="paste key here"
API_SECRET="" #API_SECRET="paste secret here"
NONCE="$(date +%s)000" #timestamp millisecond

REQUEST_URL="/external/v3/app-report?startDate=2022-10-08&endDate=2022-10-09"
REQUEST_METHOD="GET"

SIGNATURE_PAYLOAD="$API_KEY.$NONCE.$REQUEST_URL.$REQUEST_METHOD"
SIGNATURE=$(echo -n "$SIGNATURE_PAYLOAD" | openssl dgst -sha384 -hmac "$API_SECRET")

curl -X $REQUEST_METHOD \
  -H "Content-Type: application/json" \
  -H "x-rup-api-key: $API_KEY" \
  -H "x-rup-api-nonce: $NONCE" \
  -H "x-rup-api-signature: $SIGNATURE" \
  "https://api.reklamup.com$REQUEST_URL"
