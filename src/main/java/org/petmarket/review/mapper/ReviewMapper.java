package org.petmarket.review.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.petmarket.review.dto.AdvertisementReviewResponseDto;
import org.petmarket.review.dto.UserReviewResponseDto;
import org.petmarket.review.entity.Review;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "advertisementId", source = "advertisement.id")
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "authorLastName", source = "author.lastName")
    AdvertisementReviewResponseDto mapEntityToAdvertisementDto(Review entity);

    List<AdvertisementReviewResponseDto> mapEntityToAdvertisementDto(List<Review> entities);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "authorLastName", source = "author.lastName")
    UserReviewResponseDto mapEntityToUserReviewDto(Review entity);

    List<UserReviewResponseDto> mapEntityToUserReviewDto(List<Review> entities);
}
