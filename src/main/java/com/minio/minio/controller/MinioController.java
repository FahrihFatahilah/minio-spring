package com.minio.minio.controller;

import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.errors.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/minio")
public class MinioController {

    @Autowired
    private MinioClient minioClient;

    @GetMapping("/getFile")
    public String downloadFile(
            @RequestParam("bucketName") String bucketName,
            @RequestParam("objectName") String objectName) {
        String url = "";
        System.out.println("data bukername " + bucketName + " object name "+ objectName);
        int expiryInSeconds = 60 * 60; // 1 hour
        try {

            url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .method(Method.valueOf("GET"))
                            .build());
        }catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e){

        }

        System.out.println("Pre-signed URL: " + url);
        return url;
    }

        @PostMapping("/upload")
    public String handleFileUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("bucketName") String bucketName,
            @RequestParam("objectName") String objectName) {

        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .build());
            return "File uploaded successfully";
        } catch (MinioException | IOException e) {
            return "Error uploading file: " + e.getMessage();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/delete")
    public String handleFileDelete(
            @RequestParam("bucketName") String bucketName,
            @RequestParam("objectName") String objectName) {

        //            minioClient.removeObject(bucketName, objectName);
        return "File deleted successfully";
    }

    @PostMapping("/update")
    public String handleFileUpdate(
            @RequestParam("file") MultipartFile file,
            @RequestParam("bucketName") String bucketName,
            @RequestParam("objectName") String objectName) {

        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .build());
            return "File updated successfully";
        } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            return "Error updating file: " + e.getMessage();
        }
    }
}
