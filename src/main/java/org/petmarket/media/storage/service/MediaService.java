package org.petmarket.media.storage.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.engine.search.sort.dsl.SortOrder;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.images.ImageService;
import org.petmarket.media.storage.dto.MediaResponseDto;
import org.petmarket.media.storage.entity.Media;
import org.petmarket.media.storage.mapper.MediaMapper;
import org.petmarket.media.storage.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaService {
    @Value("${aws.s3.catalog.media-storage}")
    private String catalogName;
    @Value("${advertisement.media-storage.max-count}")
    private int maxImagesCount;
    @Value("${advertisement.images.small.width}")
    private int smallImageWidth;
    @Value("${advertisement.images.small.height}")
    private int smallImageHeight;
    private final MediaRepository mediaRepository;
    private final ImageService imageService;
    private final EntityManager entityManager;
    private final MediaMapper mediaMapper;

    public Page<MediaResponseDto> getAllMedia(int page, int size, String sortDirection) {
        return mediaRepository.findAllMediaResponse(PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(sortDirection), "updated")));
    }

    @Transactional
    public List<MediaResponseDto> uploadMedia(List<MultipartFile> images) {
        if (images.size() + mediaRepository.count() > maxImagesCount) {
            throw new IllegalArgumentException("the number of images in the media storage should not exceed "
                    + maxImagesCount);
        }

        List<Media> mediaList = new ArrayList<>();

        for (MultipartFile file : images) {
            Dimension dimension;

            try {
                dimension = getImageDimension(file);
            } catch (IOException e) {
                throw new IllegalArgumentException("Failed to get image dimensions", e);
            }

            Long id = generateId();
            Media media = Media.builder()
                    .name(generateName(file.getOriginalFilename(), id))
                    .url(imageService
                            .convertAndSendImage(catalogName, id, file, dimension.width, dimension.height, "b")
                            .getFullName())
                    .urlSmall(imageService
                            .convertAndSendImage(catalogName, id, file, smallImageWidth, smallImageHeight, "s")
                            .getFullName())
                    .build();
            mediaList.add(media);
        }

        mediaRepository.saveAll(mediaList);
        return mediaMapper.toMediaResponseDto(mediaList);
    }

    public Dimension getImageDimension(MultipartFile file) throws IOException {
        BufferedImage img = ImageIO.read(file.getInputStream());

        if (img != null) {
            int width = img.getWidth();
            int height = img.getHeight();
            return new Dimension(width, height);
        }

        throw new IllegalArgumentException("The file does not contain a valid image.");
    }

    @Transactional
    public void deleteMedia(Long id) {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Media with id " + id + " not found"));
        imageService.deleteImage(catalogName, media.getUrl());
        imageService.deleteImage(catalogName, media.getUrlSmall());
        mediaRepository.deleteById(id);
    }

    public Page<MediaResponseDto> searchMedia(String name, int page, int size, String sortDirection) {
        SearchSession searchSession = Search.session(entityManager);
        List<Media> mediaList = searchSession.search(Media.class)
                .where(f -> f.match().field("name").matching(name).fuzzy(2))
                .sort(f -> f.field("updated").order(SortOrder.valueOf(sortDirection)))
                .fetchHits(size * page, size);

        return new PageImpl<>(mediaMapper.toMediaResponseDto(mediaList), PageRequest.of(page, size), mediaList.size());
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
