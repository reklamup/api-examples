// Created with java 17
package com.reklamup;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

interface Constants {
    String API_KEY = ""; // String API_KEY = "paste key here"
    String API_SECRET = ""; // String API_SECRET = "paste secret here";
}

public class ReklamUpApi {
    public static void main(String[] args) {
        long nonce = System.currentTimeMillis();

        String requestUrl = "/external/v3/app-report?timeDimension=DAY&dimensions=AD_UNIT,APP,COUNTRY&startDate=2023-04-20&endDate=2023-04-20";
        String requestMethod = "GET";

        try {
            Mac mac = Mac.getInstance("HmacSHA384");
            SecretKeySpec secretKeySpec = new SecretKeySpec(Constants.API_SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA384");
            mac.init(secretKeySpec);

            String message = Constants.API_KEY + "." + nonce + "." + requestUrl + "." + requestMethod;
            byte[] hmacBytes = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));

            // The authentication signature is hashed using the apiSecret
            String signature = toHexString(hmacBytes);

            // Example request
            HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.reklamup.com" + requestUrl))
                .GET()
                .header("Content-Type", "application/json")
                .header("x-rup-api-key", Constants.API_KEY)
                .header("x-rup-api-nonce", String.valueOf(nonce))
                .header("x-rup-api-signature", signature)
                .build();

            HttpClient.newHttpClient()
                .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println)
                .join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String toHexString(byte[] bytes) {
        return new BigInteger(1, bytes).toString(16);
    }
}
