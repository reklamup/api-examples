import requests
import hmac
import hashlib
import time

apiKey = "" # apiKey = "paste key here"
apiSecret = "" # apiKey = "paste secret here"
nonce = str(time.time() * 1000) # timestamp millisecond

requestUrl = "/external/v3/app-report?startDate=2022-10-08&endDate=2022-10-09"
requestMethod = "GET"

# The authentication signature is hashed using the apiSecret
signaturePayload = apiKey + "." + nonce + "." + requestUrl + "." + requestMethod
signature = hmac.new(apiSecret.encode(), signaturePayload.encode(), hashlib.sha384).hexdigest()

headers = {
    'Content-Type': 'application/json',
    'x-rup-api-key': apiKey,
    'x-rup-api-nonce': nonce,
    'x-rup-api-signature': signature,
}

response = requests.get("https://api.reklamup.com" + requestUrl, headers=headers)

print(response.text)
