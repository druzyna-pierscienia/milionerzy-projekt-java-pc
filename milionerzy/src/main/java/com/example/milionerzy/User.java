package com.example.milionerzy;

public class User {
    private static boolean logedIn = false;
    private static String userLogin = "";

    public static void logUserIn(String login){
        logedIn = true;
        userLogin = login;
    }

    public static void logUserOut(){
        logedIn = false;
        userLogin = "";
    }
    public static String getUserLogin(){
        return userLogin;
    }
}
