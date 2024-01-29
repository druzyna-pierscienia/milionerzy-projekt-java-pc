package com.example.milionerzy;

/**
 * Represents a user in the Milionerzy application.
 * Manages the login status and user-related information.
 */
public class User {

    /**
     * A flag indicating whether a user is logged in or not.
     */
    private static boolean loggedIn = false;

    /**
     * The login name of the user.
     */
    private static String userLogin = "";

    /**
     * Logs the user into the application.
     *
     * @param login The login name of the user.
     */
    public static void logUserIn(String login) {
        loggedIn = true;
        userLogin = login;
    }

    /**
     * Logs the user out of the application.
     */
    public static void logUserOut() {
        loggedIn = false;
        userLogin = "";
    }

    /**
     * Gets the login name of the user.
     *
     * @return The login name of the user.
     */
    public static String getUserLogin() {
        return userLogin;
    }

    /**
     * Checks if the user is currently logged in.
     *
     * @return {@code true} if the user is logged in, {@code false} otherwise.
     */
    public static boolean isLoggedIn() {
        return loggedIn;
    }
}

