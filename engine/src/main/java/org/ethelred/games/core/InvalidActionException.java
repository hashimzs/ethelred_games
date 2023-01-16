package org.ethelred.games.core;

public class InvalidActionException extends RuntimeException
{
    public InvalidActionException(String message) {
        super(message);
    }

    public InvalidActionException(Throwable cause) {
        super(cause);
    }
}
