package com.example.jwttest.storage;

/**
 * User: Angelo
 * Date: 22/05/2023
 * Time: 11:50
 */
public class StorageException extends RuntimeException{

        public StorageException(String message){
            super(message);
        }

        public StorageException(String message, Throwable cause){
            super(message, cause);
        }
}
