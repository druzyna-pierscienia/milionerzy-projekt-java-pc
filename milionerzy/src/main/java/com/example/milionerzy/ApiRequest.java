package com.example.milionerzy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiRequest {

    public interface ApiCallback {
        void onResponse(String result);
        void onError(Exception e);
    }

    public static void executeRequest(String url, ApiCallback callback) {
        new Thread(() -> {
            try {
                StringBuilder result = new StringBuilder();
                URL requestUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
                connection.disconnect();

                if (callback != null) {
                    callback.onResponse(result.toString());
                }
            } catch (Exception e) {
                if (callback != null) {
                    callback.onError(e);
                }
            }
        }).start();
    }
}
