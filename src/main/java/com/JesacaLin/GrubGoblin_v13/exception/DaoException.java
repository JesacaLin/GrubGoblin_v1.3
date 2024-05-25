package com.JesacaLin.GrubGoblin_v13.exception;

/**
 * This exception is thrown when there is an error in the data access layer.
 */
public class DaoException extends RuntimeException {

     /**
     * Constructs a new DaoException with null as its detail message.
     */
    public DaoException() {
        super();
    }

     /**
     * Constructs a new DaoException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
     */
    public DaoException(String message) {
        super(message);
    }

    /**
     * Constructs a new DaoException with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the Throwable.getMessage() method).
     * @param cause the cause (which is saved for later retrieval by the Throwable.getCause() method). 
     *              (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public DaoException(String message, Exception cause) {
        super(message, cause);
    }
}
