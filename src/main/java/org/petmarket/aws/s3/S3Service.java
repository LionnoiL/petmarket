package org.petmarket.aws.s3;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.files.FileStorageName;
import org.petmarket.files.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;

@Slf4j
@Setter
@RequiredArgsConstructor
@Service
public class S3Service implements StorageService {

    private final AmazonS3 s3client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Override
    public FileStorageName sendFile(File file, String s3Catalog, String fileName) {
        String fileUrl = "";
        try {
            s3client.putObject(
                    bucketName,
                    s3Catalog + fileName,
                    file
            );
            URL url = s3client.getUrl(bucketName, s3Catalog + fileName);
            if (url != null) {
                fileUrl = url.toString();
            }
            return new FileStorageName(fileName, fileUrl);
        } catch (SdkClientException e) {
            log.error(e.toString());
            throw new S3Exception("Error send file to S3");
        }
    }

    @Override
    public byte[] getFile(String awsCatalog, String fileName) {
        try {
            S3Object s3object = s3client.getObject(bucketName, awsCatalog + fileName);
            S3ObjectInputStream inputStream = s3object.getObjectContent();
            return inputStream.readAllBytes();
        } catch (IOException | SdkClientException e) {
            log.error(e.toString());
            throw new S3Exception("Error get file from S3");
        }
    }

    @Override
    public void deleteFile(String awsCatalog, String fileName) {
        try {
            s3client.deleteObject(bucketName, awsCatalog + fileName);
        } catch (SdkClientException e) {
            log.error(e.toString());
            throw new S3Exception("Error delete file from S3");
        }
    }

    public String getFileNameAws(String urlString) {
        if (urlString == null || urlString.isEmpty()){
            return "";
        }
        String[] strings = urlString.split("/");
        if (strings.length == 0){
            return "";
        }
        return strings[strings.length - 1];
    }
}
