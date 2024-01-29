package com.example.milionerzy;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The `ApiRequestInto` class provides a simple mechanism for making asynchronous HTTP POST requests with parameters.
 * It includes an interface for callback methods to handle the response or error of the request.
 */
public class ApiRequestInto {

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
     * Executes an asynchronous HTTP POST request to the specified URL with the given parameters.
     *
     * @param url            The URL to make the POST request to.
     * @param urlParameters  The parameters to include in the POST request.
     * @param callback       The callback interface to handle the response or error.
     */
    public static void executeRequest(String url, String urlParameters, ApiCallback callback) {
        new Thread(() -> {
            try {
                // Initialize a StringBuilder to store the response
                StringBuilder result = new StringBuilder();

                // Create a URL object from the specified URL
                URL requestUrl = new URL(url);

                // Open a connection to the URL
                HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();

                // Set the request method to POST and enable output
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                // Write the URL parameters to the output stream
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(urlParameters.getBytes());
                outputStream.flush();
                outputStream.close();

                // Read the response from the input stream
                InputStream inputStream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);

                int data;
                while ((data = reader.read()) != -1) {
                    char current = (char) data;
                    result.append(current);
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
