package com.kelompok1.kloun.service.impl;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.UUID;

@Service
public class FirebaseImageService {

    private String uploadFile(File file, String fileName) {
        try {
            BlobId blobId = BlobId.of("k-lound.appspot.com", fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
            InputStream inputStream = FirebaseImageService.class.getClassLoader().getResourceAsStream("k-lound-firebase.json");
            Credentials credentials = GoogleCredentials.fromStream(inputStream);
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
            storage.create(blobInfo, Files.readAllBytes(file.toPath()));

            String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/k-lound.appspot.com/o/%s?alt=media";
            return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Upload Image To Firebase Failed, Please Try Again");
        }
    }

    public void deleteFile(String fileName) {
        try {
            InputStream serviceAccount = FirebaseImageService.class.getClassLoader().getResourceAsStream("k-lound-firebase.json");
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
            storage.delete("k-lound.appspot.com", fileName);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Delete In Firebase Failed, Please Try Again");
        }
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        return tempFile;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }


    public String upload(MultipartFile multipartFile) {
        try {
            String fileName = multipartFile.getOriginalFilename();
            fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));

            File file = this.convertToFile(multipartFile, fileName);
            String URL = this.uploadFile(file, fileName);
            file.delete();
            return URL;
        } catch (Exception e) {
            e.printStackTrace();
            return "Image Couldn't Upload, Something Went Wrong";
        }
    }
}
