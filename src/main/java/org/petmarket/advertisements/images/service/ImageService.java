package org.petmarket.advertisements.images.service;

import com.nimbusds.jose.shaded.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.entity.Advertisement;
import org.petmarket.advertisements.images.entity.AdvertisementImage;
import org.petmarket.advertisements.images.repository.AdvertisementImageRepository;
import org.petmarket.aws.s3.S3Service;
import org.petmarket.errorhandling.ImageConvertException;
import org.petmarket.files.FileStorageName;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageService {

    @Value("${aws.s3.catalog.advertisement}")
    private String catalogName;

    private final ImageResizer imageResizer;
    private final WebpConverter imageConverter;
    private final S3Service s3Service;
    private final AdvertisementImageRepository advertisementImageRepository;

    public Set<AdvertisementImage> uploadImages(Advertisement advertisement, List<MultipartFile> images) {
        Set<AdvertisementImage> result = new HashSet<>();
        boolean mainImage = true;
        for (MultipartFile file : images) {

            FileStorageName storageNameBig = convertAndSendImage(advertisement, file, 800, 800, "b");
            FileStorageName storageNameSmall = convertAndSendImage(advertisement, file, 300, 300, "s");

            Gson gson = new Gson();
            List<String> names = List.of(storageNameBig.getShortName(), storageNameSmall.getShortName());

            AdvertisementImage advertisementImage = AdvertisementImage.builder()
                    .url(storageNameBig.getFullName())
                    .urlSmall(storageNameSmall.getFullName())
                    .name(gson.toJson(names))
                    .advertisement(advertisement)
                    .mainImage(advertisement.getImages().size() == 0 && mainImage)
                    .build();
            advertisementImageRepository.save(advertisementImage);
            result.add(advertisementImage);
            mainImage = false;
        }
        return result;
    }

    private FileStorageName convertAndSendImage(Advertisement advertisement, MultipartFile file, int width, int height, String suffix) {
        File tmpFile;
        try {
            tmpFile = getTempFile("data", ".jpg");
            imageResizer.resize(file.getInputStream(), tmpFile.getAbsoluteFile().toPath(), width, height);
        } catch (Exception e) {
            throw new ImageConvertException("Image resize failed!");
        }
        File convertedImage = imageConverter.convert(tmpFile);
        tmpFile.delete();

        String newFileName = imageConverter.generateImageFileName(suffix, advertisement.getId());
        FileStorageName storageName = s3Service.sendFile(convertedImage, catalogName, newFileName);
        convertedImage.delete();
        return storageName;
    }

    private File getTempFile(String prefix, String suffix) throws IOException {
        return File.createTempFile(prefix, suffix);
    }

}
