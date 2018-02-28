package org.plugface.core;

public class CircularDependencyException extends RuntimeException {

    public CircularDependencyException(String message, Object... args) {
        super(String.format(message, args));
    }

    public CircularDependencyException(String message) {
        super(message);
    }

    public CircularDependencyException(String message, Throwable cause) {
        super(message, cause);
    }
}
