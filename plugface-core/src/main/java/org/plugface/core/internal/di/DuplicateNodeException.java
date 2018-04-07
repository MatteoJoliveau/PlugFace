package org.plugface.core.internal.di;

public class DuplicateNodeException extends RuntimeException {

    public DuplicateNodeException(String message, Object... args) {
        super(String.format(message, args));
    }

    public DuplicateNodeException(String message) {
        super(message);
    }

    public DuplicateNodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
