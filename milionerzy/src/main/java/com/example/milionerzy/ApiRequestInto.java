package com.example.milionerzy;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiRequestInto {

    public interface ApiCallback {
        void onResponse(String result);
        void onError(Exception e);
    }
    public static void executeRequest(String url, String urlParameters, ApiCallback callback) {
        new Thread(() -> {
            try {
                StringBuilder result = new StringBuilder();
                URL requestUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();

                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(urlParameters.getBytes());
                outputStream.flush();
                outputStream.close();

                InputStream inputStream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);

                int data;
                while ((data = reader.read()) != -1) {
                    char current = (char) data;
                    result.append(current);
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
