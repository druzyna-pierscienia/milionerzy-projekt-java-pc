package com.example.milionerzy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The `ApiRequest` class provides a simple mechanism for making asynchronous HTTP GET requests.
 * It includes an interface for callback methods to handle the response or error of the request.
 */
public class ApiRequest {

    /**
     * The `ApiCallback` interface defines methods to handle the response or error of an API request.
     */
    public interface ApiCallback {

        /**
         * Called when the API request is successful, providing the response as a string.
         *
         * @param result The response from the API as a string.
         */
        void onResponse(String result);

        /**
         * Called when there is an error during the API request.
         *
         * @param e The exception representing the error.
         */
        void onError(Exception e);
    }

    /**
     * Executes an asynchronous HTTP GET request to the specified URL.
     *
     * @param url      The URL to make the GET request to.
     * @param callback The callback interface to handle the response or error.
     */
    public static void executeRequest(String url, ApiCallback callback) {
        new Thread(() -> {
            try {
                // Initialize a StringBuilder to store the response
                StringBuilder result = new StringBuilder();

                // Create a URL object from the specified URL
                URL requestUrl = new URL(url);

                // Open a connection to the URL
                HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
                connection.setRequestMethod("GET");

                // Read the response from the input stream
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                // Close the reader and disconnect the connection
                reader.close();
                connection.disconnect();

                // Invoke the onResponse method of the callback with the result string
                if (callback != null) {
                    callback.onResponse(result.toString());
                }
            } catch (Exception e) {
                // Invoke the onError method of the callback with the exception
                if (callback != null) {
                    callback.onError(e);
                }
            }
        }).start();
    }
}

