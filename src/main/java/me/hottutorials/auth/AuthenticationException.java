package me.hottutorials.auth;

public class AuthenticationException extends Exception {

    public AuthenticationException() {
        super("An unknown authentication error occurred.");
    }

    public AuthenticationException(String message) {
        super(message);
    }

}
