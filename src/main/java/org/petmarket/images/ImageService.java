package org.petmarket.images;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.aws.s3.S3Service;
import org.petmarket.errorhandling.ImageConvertException;
import org.petmarket.files.FileStorageName;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageResizer imageResizer;
    private final WebpConverter imageConverter;
    private final S3Service s3Service;

    public FileStorageName convertAndSendImage(String catalogName, Long id, MultipartFile file, int width, int height,
                                               String suffix) {
        File tmpFile;
        try {
            tmpFile = getTempFile("data", ".jpg");
            imageResizer.resize(file.getInputStream(), tmpFile.getAbsoluteFile().toPath(), width, height);
        } catch (Exception e) {
            throw new ImageConvertException("Image resize failed!");
        }
        File convertedImage = imageConverter.convert(tmpFile);
        tmpFile.delete();

        String newFileName = imageConverter.generateImageFileName(suffix, id);
        FileStorageName storageName = s3Service.sendFile(convertedImage, catalogName, newFileName);
        convertedImage.delete();
        return storageName;
    }

    private File getTempFile(String prefix, String suffix) throws IOException {
        return File.createTempFile(prefix, suffix);
    }

    public void deleteImage(String awsCatalog, String url){
        String fileNameAws = s3Service.getFileNameAws(url);
        s3Service.deleteFile(awsCatalog, fileNameAws);
    }
}
