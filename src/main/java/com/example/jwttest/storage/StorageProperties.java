package com.example.jwttest.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * User: Angelo
 * Date: 22/05/2023
 * Time: 11:54
 */
@ConfigurationProperties("storage")
public class StorageProperties {

    private String location = "upload-dir";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
