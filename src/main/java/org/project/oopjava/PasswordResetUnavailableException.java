package org.project.oopjava;

public class PasswordResetUnavailableException extends RuntimeException {
    public PasswordResetUnavailableException() {
        super("Password reset delivery is not configured");
    }
}
