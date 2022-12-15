const apiKey = pm.variables.get("api_key");
const apiSecret = pm.variables.get("api_secret");
const nonce = Date.now();
const requestUrl = pm.request.url.getPathWithQuery();
const requestMethod = pm.request.method;

const signature = CryptoJS.HmacSHA384(`${apiKey}.${nonce}.${requestUrl}.${requestMethod}`, apiSecret).toString();

pm.request.headers.add({key: 'x-rup-api-key', value: apiKey});
pm.request.headers.add({key: 'x-rup-api-nonce', value: nonce});
pm.request.headers.add({key: 'x-rup-api-signature', value: signature});
