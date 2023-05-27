package com.example.jwttest.utils;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * User: Angelo
 * Date: 23/05/2023
 * Time: 10:48
 */
@Service
@Slf4j
public class FileUtils {

    public static MultipartFile renameTo(MultipartFile file, String filename) throws IOException {
        final String ext = getExtension(file.getOriginalFilename());
        final String newFileName = filename.endsWith(ext) ? filename : filename.concat(ext);

        return new MockMultipartFile(newFileName, newFileName, file.getContentType(), file.getResource().getInputStream());
    }

    public static String getExtension(@Nullable  String filename){
        if(filename == "") return "";
        int index = filename.lastIndexOf(".");
        if(index == -1){
            return "";
        } else{
            return filename.substring(index);
        }

    }
}
