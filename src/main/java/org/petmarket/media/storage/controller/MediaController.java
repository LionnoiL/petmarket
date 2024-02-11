package org.petmarket.media.storage.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.petmarket.media.storage.dto.MediaResponseDto;
import org.petmarket.media.storage.service.MediaService;
import org.petmarket.utils.annotations.parametrs.ParameterPageNumber;
import org.petmarket.utils.annotations.parametrs.ParameterPageSize;
import org.petmarket.utils.annotations.parametrs.ParameterPageSort;
import org.petmarket.utils.annotations.responses.ApiResponseForbidden;
import org.petmarket.utils.annotations.responses.ApiResponseNotFound;
import org.petmarket.utils.annotations.responses.ApiResponseSuccessful;
import org.petmarket.utils.annotations.responses.ApiResponseUnauthorized;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Media", description = "Media storage")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = "/v1/admin/media")
public class MediaController {
    private final MediaService mediaService;

    @Operation(summary = "Get all media")
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @GetMapping
    public Page<MediaResponseDto> getAllMedia(
            @ParameterPageNumber @RequestParam(defaultValue = "1") @Positive int page,
            @ParameterPageSize @RequestParam(defaultValue = "12") @Positive int size,
            @ParameterPageSort @RequestParam(defaultValue = "ASC") String sortDirection) {
        return mediaService.getAllMedia(page - 1, size, sortDirection);
    }

    @Operation(summary = "Upload media")
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<MediaResponseDto> uploadMedia(@RequestParam("images") List<MultipartFile> images,
                                              @RequestParam(value = "isSmall", required = false) boolean isSmall) {
        return mediaService.uploadMedia(images, isSmall);
    }

    @Operation(summary = "Delete media")
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @DeleteMapping("/{id}")
    public void deleteMedia(@PathVariable Long id) {
        mediaService.deleteMedia(id);
    }

    @Operation(summary = "Rename media")
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @PutMapping("/{id}")
    public MediaResponseDto renameMedia(@PathVariable Long id, @RequestParam String name) {
        return mediaService.renameMedia(id, name);
    }

    @Operation(summary = "Search media")
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @GetMapping("/search")
    public Page<MediaResponseDto> searchMedia(
            @ParameterPageNumber @RequestParam(defaultValue = "1") @Positive int page,
            @ParameterPageSize @RequestParam(defaultValue = "12") @Positive int size,
            @ParameterPageSort @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam String searchTerm) {
        return mediaService.searchMedia(searchTerm, page - 1, size, sortDirection);
    }
}
