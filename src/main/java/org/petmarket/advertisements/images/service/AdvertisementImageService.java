package org.petmarket.advertisements.images.service;

import com.nimbusds.jose.shaded.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.entity.Advertisement;
import org.petmarket.advertisements.images.entity.AdvertisementImage;
import org.petmarket.advertisements.images.repository.AdvertisementImageRepository;
import org.petmarket.errorhandling.FileUploadException;
import org.petmarket.files.FileStorageName;
import org.petmarket.images.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdvertisementImageService {

    private final AdvertisementImageRepository advertisementImageRepository;
    private final ImageService imageService;

    @Value("${aws.s3.catalog.advertisement}")
    private String catalogName;
    @Value("${advertisement.images.big.width}")
    private int bigImageWidth;
    @Value("${advertisement.images.big.height}")
    private int bigImageHeight;
    @Value("${advertisement.images.small.width}")
    private int smallImageWidth;
    @Value("${advertisement.images.small.height}")
    private int smallImageHeight;
    @Value("${advertisement.images.max-count}")
    private int maxImagesCount;

    public Set<AdvertisementImage> uploadImages(Advertisement advertisement, List<MultipartFile> images) {
        if ((images.size() + advertisement.getImages().size()) > maxImagesCount) {
            throw new FileUploadException("the number of images in the ad should not exceed " + maxImagesCount);
        }
        Set<AdvertisementImage> result = new HashSet<>();
        boolean mainImage = true;
        for (MultipartFile file : images) {

            FileStorageName storageNameBig = imageService.convertAndSendImage(catalogName, advertisement.getId(),
                    file, bigImageWidth, bigImageHeight, "b");
            FileStorageName storageNameSmall = imageService.convertAndSendImage(catalogName, advertisement.getId(),
                    file, smallImageWidth, smallImageHeight, "s");

            Gson gson = new Gson();
            List<String> names = List.of(storageNameBig.getShortName(), storageNameSmall.getShortName());

            AdvertisementImage advertisementImage = AdvertisementImage.builder()
                    .url(storageNameBig.getFullName())
                    .urlSmall(storageNameSmall.getFullName())
                    .name(gson.toJson(names))
                    .advertisement(advertisement)
                    .mainImage(advertisement.getImages().isEmpty() && mainImage)
                    .build();
            advertisementImageRepository.save(advertisementImage);
            result.add(advertisementImage);
            mainImage = false;
        }
        return result;
    }
}
