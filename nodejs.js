const CryptoJS = require('crypto-js')
const fetch = require('node-fetch')

const apiKey = "" // const apiKey = "paste key here"
const apiSecret = "" // const apiSecret = "paste secret here"
const nonce = Date.now(); // timestamp millisecond

const requestUrl = "/external/v3/app-report?startDate=2022-10-08&endDate=2022-10-09";
const requestMethod = "GET";

// The authentication signature is hashed using the apiSecret
const signature = CryptoJS.HmacSHA384(`${apiKey}.${nonce}.${requestUrl}.${requestMethod}`, apiSecret).toString();

//example request
fetch(`https://api.reklamup.com${requestUrl}`, {
    method: requestMethod,
    headers: {
        'Content-Type': 'application/json',
        'x-rup-api-key': apiKey,
        'x-rup-api-nonce': nonce,
        'x-rup-api-signature': signature,
    }
}).then(res => res.json())
    .then(json => console.log(json))
    .catch(err => console.log(err))
