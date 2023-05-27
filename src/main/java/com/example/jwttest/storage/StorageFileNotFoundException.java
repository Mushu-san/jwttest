package com.example.jwttest.storage;

/**
 * User: Angelo
 * Date: 22/05/2023
 * Time: 11:53
 */
public class StorageFileNotFoundException extends StorageException{

        public StorageFileNotFoundException(String message){
            super(message);
        }

        public StorageFileNotFoundException(String message, Throwable cause){
            super(message, cause);
        }
}
