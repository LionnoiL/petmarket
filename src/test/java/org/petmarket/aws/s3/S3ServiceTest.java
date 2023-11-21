package org.petmarket.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class S3ServiceTest {

    final String BUCKET_NAME = "test-bucket";
    final String S3_CATALOG = "test-catalog/";
    final String TEST_FILE_NAME = "test-file.txt";
    final File TEST_FILE = new File(TEST_FILE_NAME);

    @Test
    void sendFile() {
        // Arrange
        AmazonS3 mockS3Client = mock(AmazonS3.class);
        String fileName = UUID.randomUUID().toString();

        S3Service s3Service = new S3Service(mockS3Client);
        s3Service.setBucketName(BUCKET_NAME);

        // Act
        s3Service.sendFile(TEST_FILE, S3_CATALOG, fileName);

        // Assert
        verify(mockS3Client).putObject(eq(BUCKET_NAME), eq(S3_CATALOG + fileName), eq(TEST_FILE));
    }

    @Test
    void getFile() {
        // Arrange
        AmazonS3 mockS3Client = mock(AmazonS3.class);

        S3Service s3Service = new S3Service(mockS3Client);
        s3Service.setBucketName(BUCKET_NAME);

        String content = "Hello, this is a test file!";
        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);

        S3Object mockS3Object = mock(S3Object.class);
        when(mockS3Client.getObject(eq(BUCKET_NAME), eq(S3_CATALOG + TEST_FILE_NAME))).thenReturn(mockS3Object);
        when(mockS3Object.getObjectContent()).thenReturn(new S3ObjectInputStream(new ByteArrayInputStream(contentBytes), null));

        // Act
        byte[] result = s3Service.getFile(S3_CATALOG, TEST_FILE_NAME);

        // Assert
        assertArrayEquals(contentBytes, result);
        verify(mockS3Client).getObject(eq(BUCKET_NAME), eq(S3_CATALOG + TEST_FILE_NAME));
    }

    @Test
    void deleteFile() {
        // Arrange
        AmazonS3 mockS3Client = mock(AmazonS3.class);

        S3Service s3Service = new S3Service(mockS3Client);
        s3Service.setBucketName(BUCKET_NAME);

        // Act
        s3Service.deleteFile(S3_CATALOG, TEST_FILE_NAME);

        // Assert
        verify(mockS3Client).deleteObject(eq(BUCKET_NAME), eq(S3_CATALOG + TEST_FILE_NAME));
    }
}