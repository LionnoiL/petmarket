package org.petmarket.media.storage.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.petmarket.files.FileStorageName;
import org.petmarket.images.ImageService;
import org.petmarket.media.storage.dto.MediaResponseDto;
import org.petmarket.media.storage.entity.Media;
import org.petmarket.media.storage.mapper.MediaMapper;
import org.petmarket.media.storage.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaService {
    @Value("${aws.s3.catalog.media-storage}")
    private String catalogName;
    @Value("${advertisement.media-storage.max-count}")
    private int maxImagesCount;
    @Value("${advertisement.images.big.width}")
    private int bigImageWidth;
    @Value("${advertisement.images.big.height}")
    private int bigImageHeight;
    @Value("${advertisement.images.small.width}")
    private int smallImageWidth;
    @Value("${advertisement.images.small.height}")
    private int smallImageHeight;
    private final MediaRepository mediaRepository;
    private final ImageService imageService;
    private final EntityManager entityManager;
    private final MediaMapper mediaMapper;

    public Page<MediaResponseDto> getAllMedia(int page, int size) {
        return mediaRepository.findAllMediaResponse(PageRequest.of(page, size));
    }

    @Transactional
    public List<MediaResponseDto> uploadMedia(List<MultipartFile> images) {
        if (images.size() + mediaRepository.count() > maxImagesCount) {
            throw new IllegalArgumentException("the number of images in the media storage should not exceed "
                    + maxImagesCount);
        }

        List<MediaResponseDto> result = new ArrayList<>();

        for (MultipartFile file : images) {
            Long id = generateId();
            FileStorageName storageNameBig = imageService
                    .convertAndSendImage(catalogName, id, file, bigImageWidth, bigImageHeight, "b");
            FileStorageName storageNameSmall = imageService
                    .convertAndSendImage(catalogName, id, file, smallImageWidth, smallImageHeight, "s");
            Media media = Media.builder()
                    .name(generateName(file.getOriginalFilename(), id))
                    .url(storageNameBig.getFullName())
                    .urlSmall(storageNameSmall.getFullName())
                    .build();
            mediaRepository.save(media);

            MediaResponseDto mediaResponseDto = MediaResponseDto.builder()
                    .id(media.getId())
                    .url(media.getUrl())
                    .urlSmall(media.getUrlSmall())
                    .build();

            result.add(mediaResponseDto);
        }

        return result;
    }

    @Transactional
    public void deleteMedia(Long id) {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Media with id " + id + " not found"));
        imageService.deleteImage(catalogName, media.getUrl());
        imageService.deleteImage(catalogName, media.getUrlSmall());
        mediaRepository.deleteById(id);
    }

    public Page<MediaResponseDto> searchMedia(String name, int page, int size) {
        SearchSession searchSession = Search.session(entityManager);
        List<Media> mediaList = searchSession.search(Media.class)
                .where(f -> f.match().field("name").matching(name).fuzzy(2))
                .fetchHits(size * page, size);

        return new PageImpl<>(mediaList.stream().map(mediaMapper::toMediaResponseDto).toList(),
                PageRequest.of(page, size), mediaList.size());
    }

    public MediaResponseDto renameMedia(Long id, String name) {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Media with id " + id + " not found"));
        String mediaId = media.getName().split(" ")[0];
        media.setName(mediaId + " " + name);
        mediaRepository.save(media);
        return mediaMapper.toMediaResponseDto(media);
    }

    private String generateName(String originalFilename, Long id) {
        if (originalFilename == null) {
            return id + " ";
        }

        originalFilename = originalFilename.substring(0, originalFilename.indexOf('.'));
        return id + " " + originalFilename.replaceAll("[^a-zA-Z0-9.\\-]", "");
    }

    private Long generateId() {
        long id = Math.round(Math.random() * 1000000000000L);

        while (mediaRepository.existsByStartingIdInName(id)) {
            id = Math.round(Math.random() * 1000000000000L);
        }

        return id;
    }
}
