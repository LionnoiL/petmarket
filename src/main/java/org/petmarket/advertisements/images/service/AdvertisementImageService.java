package org.petmarket.advertisements.images.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.entity.Advertisement;
import org.petmarket.advertisements.images.entity.AdvertisementImage;
import org.petmarket.advertisements.images.entity.AdvertisementImageType;
import org.petmarket.advertisements.images.repository.AdvertisementImageRepository;
import org.petmarket.errorhandling.FileUploadException;
import org.petmarket.files.FileStorageName;
import org.petmarket.images.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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
    @Value("${advertisement.draft.images.daysThreshold}")
    private int daysThreshold;

    @Transactional
    public Set<AdvertisementImage> uploadImages(Advertisement advertisement,
                                                List<MultipartFile> images, AdvertisementImageType type) {
        if ((images.size() + advertisement.getImages().size()) > maxImagesCount) {
            throw new FileUploadException(
                    "the number of images in the ad should not exceed " + maxImagesCount);
        }
        Set<AdvertisementImage> result = new HashSet<>();
        boolean mainImage = true;
        for (MultipartFile file : images) {

            FileStorageName storageNameBig = imageService.convertAndSendImage(catalogName,
                    advertisement.getId(),
                    file, bigImageWidth, bigImageHeight, "b");
            FileStorageName storageNameSmall = imageService.convertAndSendImage(catalogName,
                    advertisement.getId(),
                    file, smallImageWidth, smallImageHeight, "s");

            if (type == null) {
                type = AdvertisementImageType.ADVERTISEMENT_IMAGE;
            }

            AdvertisementImage advertisementImage = AdvertisementImage.builder()
                    .type(type)
                    .url(storageNameBig.getFullName())
                    .urlSmall(storageNameSmall.getFullName())
                    .advertisement(advertisement)
                    .mainImage(!isImagesPresentByType(advertisement, type) && mainImage)
                    .build();
            advertisementImageRepository.save(advertisementImage);
            result.add(advertisementImage);
            mainImage = false;
        }
        return result;
    }

    @Transactional
    public void deleteOldDraftsImages() {
        int pageNumber = 0;
        Page<AdvertisementImage> advertisementImagePage;

        do {
            advertisementImagePage = advertisementImageRepository
                    .findImagesBeforeDeletionDate(
                            LocalDateTime.now().minusDays(daysThreshold),
                            PageRequest.of(pageNumber, 1000));
            for (AdvertisementImage image : advertisementImagePage.getContent()) {
                imageService.deleteImageFromS3(catalogName, image.getUrl());
                imageService.deleteImageFromS3(catalogName, image.getUrlSmall());
            }
            advertisementImageRepository.deleteAll(advertisementImagePage.getContent());
            pageNumber++;
        } while (advertisementImagePage.hasNext());
    }

    @Transactional
    public void deleteImagesByIds(List<Long> imageIds) {
        deleteImages(advertisementImageRepository.findAllById(imageIds));
    }

    @Transactional
    public void deleteImagesByAdvertisementId(Long advertisementId) {
        deleteImages(advertisementImageRepository.findAllByAdvertisementId(advertisementId));
    }

    @Transactional
    public void deleteImagesByAdvertisementIdAndType(Long advertisementId, AdvertisementImageType type) {
        deleteImages(advertisementImageRepository.findAllByAdvertisementIdAndType(advertisementId, type));
    }

    private void deleteImages(List<AdvertisementImage> images) {
        for (AdvertisementImage image : images) {
            imageService.deleteImageFromS3(catalogName, image.getUrl());
            imageService.deleteImageFromS3(catalogName, image.getUrlSmall());
        }

        advertisementImageRepository.deleteAll(images);
    }

    private boolean isImagesPresentByType(Advertisement advertisement,
                                          AdvertisementImageType type) {
        if (advertisement == null || type == null) {
            return false;
        }
        return advertisement.getImages().stream().anyMatch(image -> type.equals(image.getType()));
    }
}
